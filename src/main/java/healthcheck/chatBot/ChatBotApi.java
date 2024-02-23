package healthcheck.chatBot;

import healthcheck.chatBot.dto.ChatGPTRequest;
import healthcheck.chatBot.dto.ChatGptResponse;
import healthcheck.entities.Department;
import healthcheck.entities.Doctor;
import healthcheck.enums.Facility;
import healthcheck.repo.DepartmentRepo;
import healthcheck.repo.DoctorRepo;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/chat-bot")
@Slf4j
public class ChatBotApi {
    @Value("${openai.model}")
    private String model;
    @Value("${openai.api.url}")
    private String apiURL;
    private final RestTemplate template;
    private final DoctorRepo doctorRepository;
    private final DepartmentRepo departmentRepo;
    private boolean chatStarted = false;
    private String selectedDisease = "";
    private final List<String> diseaseTypes = new ArrayList<>();
    private int incorrectQueryCount = 0;
    private long blockTimestamp = 0;

    public ChatBotApi(RestTemplate template, DoctorRepo doctorRepository, DepartmentRepo departmentRepo) {
        this.doctorRepository = doctorRepository;
        this.departmentRepo = departmentRepo;
        diseaseTypes.add("Анестезиология");
        diseaseTypes.add("Онкология");
        diseaseTypes.add("Терапия");
        diseaseTypes.add("Ортопедия");
        diseaseTypes.add("Урология");
        diseaseTypes.add("Вакцинация");
        diseaseTypes.add("Оториноларингология");
        diseaseTypes.add("Флебология");
        diseaseTypes.add("Гинекология");
        diseaseTypes.add("Офтальмология");
        diseaseTypes.add("Эндокринология");
        diseaseTypes.add("Дерматология");
        diseaseTypes.add("Проктология");
        diseaseTypes.add("Физиотерапия");
        diseaseTypes.add("Кардиология");
        diseaseTypes.add("Психотерапия");
        diseaseTypes.add("Невропатия");
        diseaseTypes.add("Пульмонология");
        diseaseTypes.add("Ревматология");
        diseaseTypes.add("Нейрохирургия");
        this.template = template;
    }

    @GetMapping("/chat")
    public String getChat(Model model, HttpSession session) {
        String prompt = (String) session.getAttribute("prompt");
        if (prompt == null) {
            session.setAttribute("prompt", null);
        }
        model.addAttribute("prompt", prompt);
        model.addAttribute("response", null);
        return "chat";
    }

    @GetMapping("/chat-api")
    @ResponseBody
    public String postChat(@RequestParam("message") String message, HttpSession session) {
        if (blockTimestamp > System.currentTimeMillis()) {
            return "Вы временно заблокированы. Пожалуйста, подождите 10 секунд, прежде чем отправлять новый запрос.";
        }

        if (chatStarted || message.equalsIgnoreCase("start")) {
            if (!chatStarted) {
                session.removeAttribute("prompt");
                chatStarted = true;
            }

            if ("симптомы".equalsIgnoreCase(message.trim())) {
                String selectedDisease = (String) session.getAttribute("selectedDisease");
                if (selectedDisease == null || selectedDisease.isEmpty()) {
                    session.setAttribute("collectingSymptoms", true);
                    return "Пожалуйста, опишите ваши симптомы.";
                } else {
                    return "Вы уже выбрали направление в медицине. Для получения информации о симптомах выбранной области используйте соответствующую команду.";
                }
            }

            Boolean collectingSymptoms = (Boolean) session.getAttribute("collectingSymptoms");
            if (Boolean.TRUE.equals(collectingSymptoms)) {
                session.removeAttribute("collectingSymptoms");
                return diagnoseAndRecommendField(message);
            }

            if (selectedDisease.isEmpty()) {
                if (diseaseTypes.contains(message)) {
                    selectedDisease = message;
                    session.setAttribute("selectedDisease", selectedDisease);
                } else {
                    return "Выберите один из следующих типов направления: " + String.join(", ", diseaseTypes);
                }
            } else if ("докторс".equalsIgnoreCase(message.trim())) {
                String selectedDiseaseFromSession = (String) session.getAttribute("selectedDisease");
                if (selectedDiseaseFromSession != null && !selectedDiseaseFromSession.isEmpty()) {
                    return getDoctorsInfo(selectedDiseaseFromSession);
                } else {
                    return "Пожалуйста, сначала выберите медицинское направление.";
                }
            }

            if (selectedDisease.isEmpty()) {
                if (diseaseTypes.contains(message)) {
                    selectedDisease = message;
                    session.setAttribute("prompt", "Выберите тип болезни");
                    String query = sendOpenAiRequest("Напиши какой-то интересный и в то же время " +
                            "позитивный факт, связанный с этим направлением медицины: " + message);
                    return "А вы знали что: " + query + "Вы выбрали " + selectedDisease + ". Что вы хотите узнать о " + selectedDisease + "?";
                } else {
                    return "Выберите один из следующих типов направления: " + String.join(", ", diseaseTypes);
                }
            } else if (message.equalsIgnoreCase("menu")) {
                selectedDisease = "";
                session.removeAttribute("prompt");
                return "Выберите один из следующих типов направления: " + String.join(", ", diseaseTypes);
            } else {
                return processDiseaseKeywords(selectedDisease, message);
            }
        } else {
            return "Команда (start) для запуска chat-bot";
        }
    }

    private String processOtherKeywords(@NotNull String serviceType, String message) {
        String query = switch (serviceType.toLowerCase()) {
            case "анестезиология" ->
                    "Этот запрос должен касаться только медицинской анестезиологии или любых вопросов, связанных с ней. Если он не относится к медицине в области анестезиологии, тогда выведите слово 'Error'. Вопрос: " + message;
            case "терапия" ->
                    "Этот запрос должен быть в рамках только медицинской терапии или любых вопросов, связанных с ней. Если он не относится к медицине в области терапии, тогда выведите слово 'Error'. Вопрос: " + message;
            case "ортопедия" ->
                    "Этот запрос должен быть в рамках только медицинской ортопедии или любых вопросов, связанных с ней. Если он не относится к медицине в области ортопедии, тогда выведите слово 'Error'. Вопрос: " + message;
            case "урология" ->
                    "Этот запрос должен быть в рамках только медицинской урологии или любых вопросов, связанных с ней. Если он не относится к медицине в области урологии, тогда выведите слово 'Error'. Вопрос: " + message;
            case "вакцинация" ->
                    "Этот запрос должен быть в рамках только медицинской вакцинации или любых вопросов, связанных с ней. Если он не относится к медицине в области вакцинации, тогда выведите слово 'Error'. Вопрос: " + message;
            case "оториноларингология" ->
                    "Этот запрос должен быть в рамках только медицинской оториноларингологии или любых вопросов, связанных с ней. Если он не относится к медицине в области оториноларингологии, тогда выведите слово 'Error'. Вопрос: " + message;
            case "флебология" ->
                    "Этот запрос должен быть в рамках только медицинской флебологии или любых вопросов, связанных с ней. Если он не относится к медицине в области флебологии, тогда выведите слово 'Error'. Вопрос: " + message;
            case "гинекология" ->
                    "Этот запрос должен быть в рамках только медицинской гинекологии или любых вопросов, связанных с ней. Если он не относится к медицине в области гинекологии, тогда выведите слово 'Error'. Вопрос: " + message;
            case "офтальмология" ->
                    "Этот запрос должен быть в рамках только медицинской офтальмологии или любых вопросов, связанных с ней. Если он не относится к медицине в области офтальмологии, тогда выведите слово 'Error'. Вопрос: " + message;
            case "эндокринология" ->
                    "Этот запрос должен быть в рамках только медицинской эндокринологии или любых вопросов, связанных с ней. Если он не относится к медицине в области эндокринологии, тогда выведите слово 'Error'. Вопрос: " + message;
            case "дерматология" ->
                    "Этот запрос должен быть в рамках только медицинской дерматологии или любых вопросов, связанных с ней. Если он не относится к медицине в области дерматологии, тогда выведите слово 'Error'. Вопрос: " + message;
            case "проктология" ->
                    "Этот запрос должен быть в рамках только медицинской проктологии или любых вопросов, связанных с ней. Если он не относится к медицине в области проктологии, тогда выведите слово 'Error'. Вопрос: " + message;
            case "физиотерапия" ->
                    "Этот запрос должен быть в рамках только медицинской физиотерапии или любых вопросов, связанных с ней. Если он не относится к медицине в области физиотерапии, тогда выведите слово 'Error'. Вопрос: " + message;
            case "кардиология" ->
                    "Этот запрос должен быть в рамках только медицинской кардиологии или любых вопросов, связанных с ней. Если он не относится к медицине в области кардиологии, тогда выведите слово 'Error'. Вопрос: " + message;
            case "психотерапия" ->
                    "Этот запрос должен быть в рамках только медицинской психотерапии связанных с ней. Если он не относится к медицине в области психотерапии, тогда выведите слово 'Error'. Вопрос: " + message;
            case "невропатия" ->
                    "Этот запрос должен быть в рамках только медицинской невропатии или любых вопросов, связанных с ней. Если он не относится к медицине в области невропатии, тогда выведите слово 'Error'. Вопрос: " + message;
            case "пульмонология" ->
                    "Этот запрос должен быть в рамках только медицинской пульмонологии или любых вопросов, связанных с ней. Если он не относится к медицине в области пульмонологии, тогда выведите слово 'Error'. Вопрос: " + message;
            case "ревматология" ->
                    "Этот запрос должен быть в рамках только медицинской ревматологии или любых вопросов, связанных с ней. Если он не относится к медицине в области ревматологии, тогда выведите слово 'Error'. Вопрос: " + message;
            case "нейрохирургия" ->
                    "Этот запрос должен быть в рамках только медицинской нейрохирургии или любых вопросов, связанных с ней. Если он не относится к медицине в области нейрохирургии, тогда выведите слово 'Error'. Вопрос: " + message;
            default -> "Error";
        };
        return validateAndProcessQuery(sendOpenAiRequest(query));
    }

    private String sendOpenAiRequest(String userMessage) {
        try {
            log.info("Sending user message to OpenAI: {}", userMessage);

            ChatGPTRequest request = new ChatGPTRequest(model, userMessage);
            ChatGptResponse chatGptResponse = template.postForObject(apiURL, request, ChatGptResponse.class);

            if (chatGptResponse != null) {
                String responseContent = chatGptResponse.getChoices().get(0).getMessage().getContent();
                log.info("Received response from OpenAI: {}", responseContent);
                return responseContent;
            } else {
                log.warn("Received null response from OpenAI");
                return null;
            }
        } catch (Exception e) {
            log.error("Error while sending request to OpenAI", e);
            return "Извините, произошла ошибка при обращении к сервису ИИ.";
        }
    }

    private String validateAndProcessQuery(String query) {
        if ("Error".equals(query) || "(Error)".equals(query) || "Error.".equals(query) || "Ошибка.".equals(query)) {
            incorrectQueryCount++;
            if (incorrectQueryCount >= 3) {
                blockTimestamp = System.currentTimeMillis() + 10000;
                return "Вы превысили лимит неправильных запросов. Пожалуйста, подождите 10 секунд, прежде чем отправлять новый запрос.";
            }
            return "Мой фокус - помогать с медицинской информацией, давайте оставаться в рамках этой темы!";
        }
        incorrectQueryCount = 0;
        return query;
    }

    private String buildMedicineQuery(String serviceType) {
        String formattedServiceType = serviceType.toLowerCase();
        String query = String.format("Объясни какие симптомы могут быть у этого направления: %s", formattedServiceType);
        return sendOpenAiRequest(query);
    }

    private String processDiseaseKeywords(String diseaseType, @NotNull String message) {
        if (message.equalsIgnoreCase("симптомы")) {
            return buildMedicineQuery(diseaseType);
        }
        return processOtherKeywords(diseaseType, message);
    }

    private String diagnoseAndRecommendField(String symptoms) {
        String diagnosis = sendOpenAiRequest("Диагностика заболевания на основе следующих симптомов" + symptoms +
                " (пусть симптомы будут касательно только медицины) На основе этих симптомов порекомендуй пойти к одному из специалистов в этом списке (" +
                "Анестезиология\n" +
                "Онкология\n" +
                "Терапия\n" +
                "Ортопедия\n" +
                "Урология\n" +
                "Вакцинация\n" +
                "Оториноларингология\n" +
                "Флебология\n" +
                "Гинекология\n" +
                "Офтальмология\n" +
                "Эндокринология\n" +
                "Дерматология\n" +
                "Проктология\n" +
                "Физиотерапия\n" +
                "Кардиология\n" +
                "Психотерапия\n" +
                "Невропатия\n" +
                "Пульмонология\n" +
                "Ревматология\n" +
                "Нейрохирургия)");
        String recommendedField = findClosestMatch(diagnosis);
        return "Ваша диагностика: " + diagnosis + " На основе диагноза мы рекомендуем следующее медицинское направление: " + recommendedField;
    }

    private String findClosestMatch(String diagnosis) {
        return diseaseTypes.stream()
                .filter(diagnosis::contains)
                .findFirst()
                .orElse("неопределенное");
    }

    private String getDoctorsInfo(String departmentName) {
        Department department = departmentRepo.findByFacility(Facility.valueOf(departmentName));
        List<Doctor> doctors = doctorRepository.getDoctorsByDepartment(department);
        if (doctors.isEmpty()) {
            return "В данный момент нет доступных докторов в отделении " + departmentName;
        } else {
            return doctors.stream()
                    .map(Doctor::getFullNameDoctor)
                    .collect(Collectors.joining(", ", "Доступные доктора в отделении " + departmentName + ": ", "."));
        }
    }
}