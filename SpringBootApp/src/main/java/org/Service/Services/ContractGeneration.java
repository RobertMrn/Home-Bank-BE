package org.Service.Services;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.draw.VerticalPositionMark;
import org.Service.Entities.CreditLoan;
import org.Service.Entities.User;
import org.apache.commons.lang3.time.DateUtils;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ContractGeneration {

    public Document createPDFDocument(HttpServletResponse response) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());
        return document;
    }

    public List<String> populatePDF(User user, CreditLoan creditLoan) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(creditLoan.getCreationDate());
        String titleText = "LOAN AGREEMENT";
        String bankInfoText = "Poli Bank located in Iuliu Maniu Boulevard 1-3, Bucharest 061071, registered at the Commercial Register" +
                "number. 1, unique registration code 1234, registered in Bank Register number. CZ-ABC/01.01.2023, represented by Students as CEO";
        String userInfoText = user.getFirstName() + " " + user.getLastName() + " located in " + user.getAddress() + ", personal unique code " + user.getPersonalUniqueCode() +
                ", phone number" + user.getPhoneNumber() + ", as a BORROWER, the following loan agreement intervened:\n";
        String creditLoanInfoText = "1.\tPromise to Pay: Within " + creditLoan.getTenure() + " months from today, Borrower promises to pay to Lender " + creditLoan.getAmount() + " EUR and interest as well as other charges avowed below.\n" +
                "2.\tAccountability: Although this agreement may be signed below by more than one person, each of the undersigned understands that they are each as individuals responsible and jointly and severally liable for paying back the full amount.\n" +
                "3.\tBreakdown of Loan: Borrower will pay: \n" +
                "Amount of Loan: " + creditLoan.getAmount() + " EURO\n" +
                "ANNUAL PERCENTAGE RATE: " + creditLoan.getInterestRate() + "%\n" +
                "Total of payments: " + creditLoan.getAmountToBePaid() + "\n" +
                "4.\tRepayment: Borrower will pay back in the following manner: Borrower will repay the amount of this note in " + creditLoan.getTenure() + " equal continuous monthly installments of " + creditLoan.getInstallment() + " EUR each on the " + calendar.get(Calendar.DAY_OF_MONTH) +
                " day of each month, and ending on " + DateUtils.addMonths(creditLoan.getCreationDate(), creditLoan.getTenure()) + "\n" +
                "5.\tPrepayment: Borrower has the right to pay back the whole exceptional amount at any time. If Borrower pays before time, or if this loan is refinanced or replaced by a new note, Lender will refund the unearned finance charge, figured by the Rule of 78-a commonly used formula for figuring rebates on installment loans.\n" +
                "6.\tCollection fees: If this note is placed with a legal representative for collection, then Borrower agrees to pay an attorney's fee of fifteen percent (15%) of the voluntary balance. This fee will be added to the unpaid balance of the loan.\n" +
                "6.\tCo-borrowers: Any Co-borrowers signing this agreement agree to be likewise accountable with the borrower for this loan.\n";
        String signatureOfBankText = "In name of \n" + "Poli Bank";
        return Arrays.asList(titleText, bankInfoText, userInfoText, creditLoanInfoText, signatureOfBankText);
    }

    public void exportPDF(HttpServletResponse response, User user, CreditLoan creditLoan) throws DocumentException, IOException {
        List<String> contentPDF = populatePDF(user, creditLoan);
        Document pdfForExport = createPDFDocument(response);
        pdfForExport.setMargins(70, 70, 72, 36);
        pdfForExport.setFooter(new HeaderFooter(new Phrase(new Chunk(new VerticalPositionMark())), true));
        pdfForExport.open();

        pdfForExport.addTitle(user.getLastName() +"_"+ creditLoan.getContractId());
        pdfForExport.add(new Paragraph("\n"));

        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        titleFont.setSize(18);
        titleFont.setColor(Color.BLACK);
        Paragraph title = new Paragraph(contentPDF.get(0), titleFont);
        title.setAlignment(Paragraph.ALIGN_CENTER);
        pdfForExport.add(title);
        pdfForExport.add(new Paragraph("\n"));

        Font fontBankInfo = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        fontBankInfo.setSize(10);
        fontBankInfo.setColor(Color.BLACK);
        Paragraph bankInfo = new Paragraph(contentPDF.get(1), fontBankInfo);
        bankInfo.setAlignment(Paragraph.ALIGN_JUSTIFIED);
        pdfForExport.add(bankInfo);
        pdfForExport.add(new Paragraph("\n"));

        Paragraph spaceBetweenBankInfoAndUserInfo = new Paragraph("AND");
        spaceBetweenBankInfoAndUserInfo.setAlignment(Paragraph.ALIGN_CENTER);
        pdfForExport.add(spaceBetweenBankInfoAndUserInfo);
        pdfForExport.add(new Paragraph("\n"));

        Font fontUserInfo = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        fontUserInfo.setColor(Color.BLACK);
        fontUserInfo.setSize(10);
        Paragraph userInfo = new Paragraph(contentPDF.get(2), fontUserInfo);
        userInfo.setAlignment(Paragraph.ALIGN_JUSTIFIED);
        pdfForExport.add(userInfo);
        pdfForExport.add(new Paragraph("\n"));
        pdfForExport.add(new Paragraph("\n"));

        Font fontCreditLoanInfo = FontFactory.getFont(FontFactory.HELVETICA);
        fontCreditLoanInfo.setSize(10);
        fontCreditLoanInfo.setColor(Color.BLACK);
        Paragraph creditLoanInfo = new Paragraph(contentPDF.get(3), fontCreditLoanInfo);
        creditLoanInfo.setAlignment(Paragraph.ALIGN_JUSTIFIED);
        pdfForExport.add(creditLoanInfo);
        pdfForExport.add(new Paragraph("\n"));
        pdfForExport.add(new Paragraph("\n"));
        pdfForExport.add(new Paragraph("\n"));

        Paragraph signatureOfBankAndBorrower = new Paragraph(contentPDF.get(4));
        Chunk glue = new Chunk(new VerticalPositionMark());
        signatureOfBankAndBorrower.add(new Chunk(glue));
        signatureOfBankAndBorrower.add(" Borrower");
        pdfForExport.add(signatureOfBankAndBorrower);

        pdfForExport.close();
    }

}
