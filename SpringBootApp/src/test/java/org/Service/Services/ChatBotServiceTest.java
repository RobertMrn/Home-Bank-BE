package org.Service.Services;

import org.DTOs.ChatBotResponse;
import org.DTOs.CreditLoansForUser;
import org.Service.Entities.ChatBot;
import org.Service.Entities.CreditLoan;
import org.Service.Entities.UserRole;
import org.Service.Repositories.ChatBotRepo;
import org.Service.Repositories.CreditLoanRepo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ChatBotServiceTest {

    @Mock
    private ChatBotRepo chatBotRepo;

    @Mock
    private CreditLoanService creditLoanService;

    @Mock
    private UserService userService;

    @Mock
    private UserRoleService userRoleService;

    @Mock
    private CreditLoanRepo creditLoanRepo;

    @InjectMocks
    private ChatBotService chatBotService;

    @Test
    public void testFindNumberOfYellowDecisionContracts() {
        int expectedNumberOfContracts = 5;
        when(creditLoanRepo.findNumberOfYellowDecisionContracts()).thenReturn(expectedNumberOfContracts);

        String result = chatBotService.findNumberOfYellowDecisionContracts();

        assertEquals(String.valueOf(expectedNumberOfContracts), result);
    }

    @Test
    public void testFindPayment() {
        int contractId = 1;
        BigDecimal expectedPayment = new BigDecimal(1000);
        CreditLoan creditLoan = new CreditLoan();
        creditLoan.setAmountToBePaid(expectedPayment);
        when(creditLoanService.findCreditLoanById(contractId)).thenReturn(creditLoan);

        BigDecimal result = chatBotService.findPayment(contractId);

        assertEquals(expectedPayment, result);
    }

    @Test
    public void testFindDecisionOfContract() {
        int contractId = 1;
        String expectedDecision = "Approved";
        CreditLoan creditLoan = new CreditLoan();
        creditLoan.setEsDecision(expectedDecision);
        when(creditLoanService.findCreditLoanById(contractId)).thenReturn(creditLoan);

        String result = chatBotService.findDecisionOfContract(contractId);

        assertEquals(expectedDecision, result);
    }

    @Test
    public void testFindDayOfInstallmentDate() {
        int contractId = 1;
        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        int expectedDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        CreditLoan creditLoan = new CreditLoan();
        creditLoan.setCreationDate(currentDate);
        when(creditLoanService.findCreditLoanById(contractId)).thenReturn(creditLoan);

        String result = chatBotService.findDayOfInstallmentDate(contractId);

        assertEquals(String.valueOf(expectedDayOfMonth), result);
    }

    @Test
    public void testFindNumberOfCreditsForUser() {
        int userId = 1;
        List<CreditLoansForUser> creditLoanList = new ArrayList<>();
        creditLoanList.add(new CreditLoansForUser());
        when(creditLoanService.findCreditLoansByUserId(userId)).thenReturn(creditLoanList);

        int result = chatBotService.findNumberOfCreditsForUser(userId);

        assertEquals(creditLoanList.size(), result);
    }

    @Test
    public void testGetResponseForUndefinedQuestion() {
        String question = "What is my contract?";
        int userId = 1;
        UserRole userRole = new UserRole();
        userRole.setRole("Customer");
        when(userRoleService.findUserRoleById(userId)).thenReturn(userRole);

        ChatBotResponse result = chatBotService.getResponseForUndefinedQuestion(question, userId);

        assertNotNull(result);
    }

    @Test
    public void testGetChatBotResponse_questionNotFound() {
        String question = "How are you?";
        int userId = 1;
        List<ChatBot> allQuestions = new ArrayList<>();
        when(chatBotRepo.findAll()).thenReturn(allQuestions);

        ChatBotResponse result = chatBotService.getChatBotResponse(question, userId);

        assertNotNull(result);
        assertEquals("Sorry, I can't understand. Can you repeat please?", result.getAnswer());
    }

    @Test
    public void testGetChatBotResponse_undefinedQuestion_exactMatch() {
        List<ChatBot> allQuestions = new ArrayList<>();
        ChatBot undefinedQuestion = new ChatBot();
        undefinedQuestion.setType("Undefined");
        allQuestions.add(undefinedQuestion);
    }

}
