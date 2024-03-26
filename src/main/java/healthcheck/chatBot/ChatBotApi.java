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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
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
    private final List<String> diseaseTypes = new ArrayList<>();
    private int incorrectQueryCount = 0;
    private long blockTimestamp = 0;

    public ChatBotApi(RestTemplate template, DoctorRepo doctorRepository, DepartmentRepo departmentRepo) {
        this.template = template;
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
    public Object postChat(@RequestParam("message") String message, HttpSession session) {
        if (blockTimestamp > System.currentTimeMillis()) {
            return "Вы временно заблокированы. Пожалуйста, подождите 10 секунд, прежде чем отправлять новый запрос.";
        }

        if (chatStarted || message.equalsIgnoreCase("start")) {
            if (!chatStarted) {
                session.removeAttribute("prompt");
                chatStarted = true;
            }

            String selectedDisease = (String) session.getAttribute("selectedDisease");

            if ("симптомы".equalsIgnoreCase(message.trim())) {
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

            if ("menu".equalsIgnoreCase(message.trim())) {
                session.removeAttribute("selectedDisease");
                session.removeAttribute("prompt");
                return "Выберите один из следующих типов направления: " + String.join(", ", diseaseTypes);
            }

            if (selectedDisease == null || selectedDisease.isEmpty()) {
                if (diseaseTypes.contains(message)) {
                    session.setAttribute("selectedDisease", message);
                    return "Вы выбрали " + message + ". Что вы хотите узнать о " + message + "?";
                } else {
                    return "Выберите один из следующих типов направления: " + String.join(", ", diseaseTypes);
                }
            } else {
                return processDiseaseKeywords(selectedDisease, message);
            }
        } else {
            return "Команда (start) для запуска chat-bot";
        }
    }

    private String processOtherKeywords(@NotNull String serviceType, String message) {
        String messageUpdate = message + "это вопрос в - " + serviceType.toLowerCase();
        String query = "Этот запрос должен касаться только медицинской " + serviceType.toLowerCase() + " или любых вопросов, связанных с ней. Если он не относится к медицине в области " + serviceType.toLowerCase() + ", тогда выведите слово 'Error'. Вопрос: " + messageUpdate;
        return validateAndProcessQuery(sendOpenAiRequest(query));
    }

    private String buildMedicineQueryC(String serviceType) {
        String formattedServiceType = serviceType.toLowerCase();
        String query = String.format("Объясни какие симптомы могут быть у этого направления: %s", formattedServiceType);
        return sendOpenAiRequest(query);
    }

    private String buildMedicineQueryL(String serviceType) {
        String formattedServiceType = serviceType.toLowerCase();
        String query = String.format("Порекомендуй какие лекарства могут помочь с проблемами связанные в этом направлении" +
                ". Но предупреди, что это просто рекомендация: %s", formattedServiceType);
        return sendOpenAiRequest(query);
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

    private List<String> getDoctorsInfoTimeSheet(String departmentName) {
        Department department = departmentRepo.findByFacility(Facility.valueOf(departmentName));
        LocalDate currentDate = LocalDate.now();
        LocalDate currentDateRestrictions = currentDate.plusWeeks(1);

        return doctorRepository.getDoctorsByDepartment(department).stream()
                .flatMap(doctor -> doctor.getSchedule().getTimeSheets().stream()
                        .filter(timeSheet -> timeSheet.getDateOfConsultation().isAfter(currentDate)
                                && timeSheet.getDateOfConsultation().isBefore(currentDateRestrictions))
                        .map(timeSheet -> {
                            String formattedDate = timeSheet.getDateOfConsultation().format(DateTimeFormatter.ofPattern("dd.MM"));
                            String timeSlotInfo = formattedDate + " - " + timeSheet.getStartTimeOfConsultation();
                            if (doctor.getSchedule().getTimeSheets().indexOf(timeSheet) == 0) {
                                return doctor.getFullNameDoctor() + ": " + timeSlotInfo;
                            } else {
                                return timeSlotInfo;
                            }
                        })
                        .toList()
                        .stream())
                .collect(Collectors.collectingAndThen(Collectors.toList(), list -> list.isEmpty() ?
                        Collections.singletonList("В данный момент нет свободных записей на ближайшею неделю") : list));
    }

    private Object processDiseaseKeywords(String diseaseType, @NotNull String message) {
        return switch (message.toLowerCase()) {
            case "симптомы" -> buildMedicineQueryC(diseaseType);
            case "лекарства" -> buildMedicineQueryL(diseaseType);
            case "доктора" -> getDoctorsInfo(diseaseType);
            case "запись" -> getDoctorsInfoTimeSheet(diseaseType);
            default -> processOtherKeywords(diseaseType, message);
        };
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
        return "Ваша диагностика: " + diagnosis;
    }
}