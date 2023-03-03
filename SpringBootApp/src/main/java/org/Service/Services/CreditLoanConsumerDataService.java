package org.Service.Services;

import org.DTOs.ConsumerDataDTO;
import org.DTOs.CreditLoansForUser;
import org.DTOs.UserDataAndConsumerData;
import org.Service.Entities.CreditLoan;
import org.Service.Entities.CreditLoanConsumerData;
import org.Service.Entities.User;
import org.Service.Repositories.CreditLoanConsumerDataRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class CreditLoanConsumerDataService {

    @Autowired
    CreditLoanConsumerDataRepo creditLoanConsumerDataRepo;

    @Autowired
    CreditLoanService creditLoanService;

    @Autowired
    UserService userService;

    @Autowired
    UserRoleService userRoleService;

    public CreditLoanConsumerData addConsumerData(ConsumerDataDTO consumerDataDTO) {
        CreditLoan creditLoan = creditLoanService.findCreditLoanById(consumerDataDTO.getContractId());
        CreditLoanConsumerData creditLoanConsumerData = new CreditLoanConsumerData(creditLoan, consumerDataDTO.getConsumerDataId(), consumerDataDTO.getIban(),
                consumerDataDTO.getFamilySituation(), consumerDataDTO.getLivingSituation(), consumerDataDTO.getNumberOfChildren(), consumerDataDTO.getOccupation(), consumerDataDTO.getEmploymentStartDate(),
                consumerDataDTO.getMonthlyIncome());
        return creditLoanConsumerDataRepo.save(creditLoanConsumerData);
    }

    public CreditLoanConsumerData findConsumerDataById(int id) {
        Optional<CreditLoanConsumerData> creditLoan = creditLoanConsumerDataRepo.findById(id);
        if (creditLoan.isPresent()) {
            return creditLoan.get();
        } else {
            throw new NoSuchElementException("consumer data not found");
        }
    }

    public CreditLoanConsumerData findConsumerDataByCreditLoan(CreditLoan creditLoan) {
        Optional<CreditLoanConsumerData> creditLoanConsumerData = Optional.ofNullable(creditLoanConsumerDataRepo.findCreditLoanConsumerDataByContractId(creditLoan));
        if (creditLoanConsumerData.isPresent()) {
            return creditLoanConsumerData.get();
        } else {
            throw new NoSuchElementException("consumer data not found");
        }
    }

    public UserDataAndConsumerData findUserDataAndConsumerDataByContractId(int contractId) {
        CreditLoan creditLoan = creditLoanService.findCreditLoanById(contractId);
        User user = userService.findUserById(creditLoan.getUser().getUserId());
        CreditLoanConsumerData creditLoanConsumerData = findConsumerDataByCreditLoan(creditLoan);
        UserDataAndConsumerData userDataAndConsumerData = new UserDataAndConsumerData(
                user.getUserId(), user.getEmail(), user.getFirstName(), user.getLastName(),
                user.getPhoneNumber(), user.getGender(), user.getAddress(), user.getNationality(),
                user.getBirthDate(), creditLoanConsumerData.getIban(), creditLoanConsumerData.getFamilySituation(),
                creditLoanConsumerData.getOccupation(), creditLoanConsumerData.getEmploymentStartDate(),
                creditLoanConsumerData.getMonthlyIncome());
        return userDataAndConsumerData;
    }

    public void deleteConsumerDataAndCreditLoansAndUserById(int userId) {
        List<CreditLoansForUser> allUserCreditLoans = creditLoanService.findCreditLoansByUserId(userId);
        for (CreditLoansForUser creditLoansForUser : allUserCreditLoans){
            CreditLoan creditLoan = creditLoanService.findCreditLoanById(creditLoansForUser.getContractId());
            CreditLoanConsumerData creditLoanConsumerData = findConsumerDataByCreditLoan(creditLoan);
            creditLoanConsumerDataRepo.delete(creditLoanConsumerData);
        }
        for (CreditLoansForUser creditLoansForUser : allUserCreditLoans){
            creditLoanService.deleteCreditLoanById(creditLoansForUser.getContractId());
        }
        userRoleService.deleteUserRoleById(userId);
        //userService.deleteUserById(userId);
    }
}
