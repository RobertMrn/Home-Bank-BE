package org.Service.Services;

import org.DTOs.ChatBotResponse;
import org.DTOs.CreditLoansForUser;
import org.Service.Entities.ChatBot;
import org.Service.Entities.UserRole;
import org.Service.Repositories.ChatBotRepo;
import org.Service.Repositories.CreditLoanRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class ChatBotService {
    private String contractId = String.valueOf(' ');

    @Autowired
    private ChatBotRepo chatBotRepo;

    @Autowired
    private CreditLoanService creditLoanService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private CreditLoanRepo creditLoanRepo;

    public String findNumberOfYellowDecisionContracts() {
        return String.valueOf(creditLoanRepo.findNumberOfYellowDecisionContracts());
    }

    public BigDecimal findPayment(int contractId) {
        BigDecimal payment = creditLoanService.findCreditLoanById(contractId).getAmountToBePaid();
        return payment;
    }

    public String findDecisionOfContract(int contractId) {
        String decision = creditLoanService.findCreditLoanById(contractId).getEsDecision();
        return decision;
    }

    public String findDayOfInstallmentDate(int contractId) {
        Date date = creditLoanService.findCreditLoanById(contractId).getCreationDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
    }

    public int findNumberOfCreditsForUser(int userId) {
        List<CreditLoansForUser> creditLoanList = creditLoanService.findCreditLoansByUserId(userId);
        return creditLoanList.size();
    }

    public ChatBotResponse getResponseForUndefinedQuestion(String question, int userId) {
        UserRole userRole = userRoleService.findUserRoleById(userId);
        if(!userRole.getRole().equals("Customer")){
            return new ChatBotResponse("I'm sorry, but you don't have the privilege to access this information");
        }
        String[] questionAsArray = question.split(" ");
        List<CreditLoansForUser> creditLoansForUsers = creditLoanService.findCreditLoansByUserId(userId);
        for (CreditLoansForUser creditLoansForUser : creditLoansForUsers) {
            for (String i : questionAsArray) {
                if (i.equals(String.valueOf(creditLoansForUser.getContractId()))) {
                    this.contractId = String.valueOf(creditLoansForUser.getContractId());
                    break;
                }
            }
        }
        if (contractId.equals(String.valueOf(' '))) {
            return new ChatBotResponse("I'm sorry, but I could not find your contract, please make sure you have entered the right contract id");
        }

        return new ChatBotResponse("I understand that you want to find out some information about your contract." +
                " Please, press 1 to find out how much money you have to pay for your contract. Press 2 to find out the decision" +
                " of the contract and 3 to find out the day of last installment date");
    }

    public ChatBotResponse getResponseForDynamicQuestion(ChatBot mostSimilarQuestionFromDatabase, int userId) {
        if (mostSimilarQuestionFromDatabase.getAnswer().equals("findLoansWithYellowDecision")) {
            UserRole userRole = userRoleService.findUserRoleById(userId);
            if (!userRole.getRole().equals("Customer")) {
                return new ChatBotResponse("There are " + findNumberOfYellowDecisionContracts() + " yellow decision contracts");
            } else {
                return new ChatBotResponse("I'm sorry, but you don't have the privilege to access this information");
            }
        }
        if (mostSimilarQuestionFromDatabase.getAnswer().equals("findNumberOfCreditForUser")) {
            return new ChatBotResponse("You have " + findNumberOfCreditsForUser(userId) + " contracts");
        }
        return new ChatBotResponse("Sorry, I can't understand, can you repeat please?");
    }

    public ChatBotResponse getChatBotResponse(String question, int userId) {
        if (contractId != null && !contractId.isEmpty() && !contractId.isBlank()) {
            switch (question) {
                case "1":
                    return new ChatBotResponse(" For the contract " + contractId + ", you have to pay " + findPayment(Integer.parseInt(contractId)) + "EUR");
                case "2":
                    return new ChatBotResponse(" For the contract " + contractId + ", the decision is " + findDecisionOfContract(Integer.parseInt(contractId)));
                case "3":
                    return new ChatBotResponse(" For the contract " + contractId + ", the day of last installment date is " + findDayOfInstallmentDate(Integer.parseInt(contractId)) +
                            " of the current month, or if you already paid, then the next month.");
            }
            contractId = String.valueOf(' ');
        }

        List<ChatBot> allQuestions = (List<ChatBot>) chatBotRepo.findAll();
        StringBuilder theMostSimilarQuestionToTheOneFromHttpClient = new StringBuilder();
        int counterWhichHoldsTheMaximumNumberOfWordsInCommon = 0;
        int lengthOfTheQuestionWhichHasTheMaximumNumberOfWordsInCommonWithTheHttpClientQuestion = 0;
        ChatBot mostSimilarQuestionFromDB = new ChatBot();
        for (ChatBot questionFromDatabase : allQuestions) {
            int counterWhichHoldsHowManyWordsHaveInCommonTheTwoQuestions = 0;
            String[] questionFromDatabaseAsArray = questionFromDatabase.getQuestion().split(" ");
            for (String i : questionFromDatabaseAsArray) {
                if (question.contains(i)) {
                    counterWhichHoldsHowManyWordsHaveInCommonTheTwoQuestions++;
                }
            }
            if (counterWhichHoldsHowManyWordsHaveInCommonTheTwoQuestions > counterWhichHoldsTheMaximumNumberOfWordsInCommon) {
                counterWhichHoldsTheMaximumNumberOfWordsInCommon = counterWhichHoldsHowManyWordsHaveInCommonTheTwoQuestions;
                lengthOfTheQuestionWhichHasTheMaximumNumberOfWordsInCommonWithTheHttpClientQuestion = questionFromDatabaseAsArray.length;
                theMostSimilarQuestionToTheOneFromHttpClient = new StringBuilder(questionFromDatabase.getQuestion());
                mostSimilarQuestionFromDB = questionFromDatabase;
            }
        }

        if (lengthOfTheQuestionWhichHasTheMaximumNumberOfWordsInCommonWithTheHttpClientQuestion != 0) {
            if ((question.split(" ").length / lengthOfTheQuestionWhichHasTheMaximumNumberOfWordsInCommonWithTheHttpClientQuestion) == 1) {
                if (mostSimilarQuestionFromDB.getType().equals("Undefined")) {
                    return getResponseForUndefinedQuestion(question, userId);
                }
                if (mostSimilarQuestionFromDB.getType().equals("Dynamic")) {
                    return getResponseForDynamicQuestion(mostSimilarQuestionFromDB, userId);
                }
                ChatBotResponse chatBotResponse = new ChatBotResponse(chatBotRepo.findByQuestion(String.valueOf(theMostSimilarQuestionToTheOneFromHttpClient)).getAnswer());
                return chatBotResponse;
            }
            if ((double) (question.length() / lengthOfTheQuestionWhichHasTheMaximumNumberOfWordsInCommonWithTheHttpClientQuestion) > 0.5) {
                if (mostSimilarQuestionFromDB.getType().equals("Undefined")) {
                    return new ChatBotResponse("I guess you want to find out the answer of this question: " + chatBotRepo.findByQuestion(mostSimilarQuestionFromDB.getQuestion()).getQuestion() +
                            "\n" + ". So here's the answer: " + getResponseForUndefinedQuestion(question, userId).getAnswer());
                }
                if (mostSimilarQuestionFromDB.getType().equals("Dynamic")) {
                    return new ChatBotResponse("I guess you want to find out the answer of this question: " + chatBotRepo.findByQuestion(mostSimilarQuestionFromDB.getQuestion()).getQuestion() +
                            "\n" + ". So here's the answer: " + getResponseForDynamicQuestion(mostSimilarQuestionFromDB, userId).getAnswer());
                }
                ChatBotResponse chatBotResponse = new ChatBotResponse("I guess you want to find out the answer of this question: " + chatBotRepo.findByQuestion(String.valueOf(theMostSimilarQuestionToTheOneFromHttpClient)).getQuestion() +
                        "\n" + ". So here's the answer: " + chatBotRepo.findByQuestion(String.valueOf(theMostSimilarQuestionToTheOneFromHttpClient)).getAnswer());
                return chatBotResponse;
            }
        }
        return new ChatBotResponse("Sorry, I can't understand. Can you repeat please?");
    }
}
