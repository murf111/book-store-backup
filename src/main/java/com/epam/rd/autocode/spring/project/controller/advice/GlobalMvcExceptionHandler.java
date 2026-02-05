package com.epam.rd.autocode.spring.project.controller.advice;

import com.epam.rd.autocode.spring.project.exception.AlreadyExistException;
import com.epam.rd.autocode.spring.project.exception.FileStorageException;
import com.epam.rd.autocode.spring.project.exception.InsufficientFundsException;
import com.epam.rd.autocode.spring.project.exception.NotFoundException;
import com.epam.rd.autocode.spring.project.exception.PaymentDeclinedException;
import com.epam.rd.autocode.spring.project.util.Routes;
import com.epam.rd.autocode.spring.project.util.ViewNames;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalMvcExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleNotFound(NotFoundException e) {
        ModelAndView mav = new ModelAndView(ViewNames.VIEW_ERROR_404);
        mav.addObject("errorMessage", e.getMessage());
        return mav;
    }

    @ExceptionHandler(AlreadyExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ModelAndView handleAlreadyExist(AlreadyExistException e) {
        ModelAndView mav = new ModelAndView(ViewNames.VIEW_GENERAL_ERROR);
        mav.addObject("errorMessage","Action Failed: " + e.getMessage());
        return mav;
    }

    @ExceptionHandler(InsufficientFundsException.class)
    @ResponseStatus(HttpStatus.PAYMENT_REQUIRED)
    public ModelAndView handleInsufficientFunds(InsufficientFundsException e) {
        ModelAndView mav = new ModelAndView(ViewNames.VIEW_GENERAL_ERROR);
        mav.addObject("errorMessage", "Transaction Failed: " + e.getMessage());
        return mav;
    }

    @ExceptionHandler(PaymentDeclinedException.class)
    public String handlePaymentError(PaymentDeclinedException e,
                                     RedirectAttributes redirectAttributes,
                                     HttpServletRequest request) {

        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());

        String referer = request.getHeader("Referer");

        return "redirect:" + (referer != null ? referer : Routes.PROFILE);
    }

    @ExceptionHandler(FileStorageException.class)
    public ModelAndView handleFileStorageError(FileStorageException e) {
        ModelAndView mav = new ModelAndView(ViewNames.VIEW_GENERAL_ERROR);
        mav.addObject("errorMessage", "Image Upload Failed: " + e.getMessage());
        return mav;
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ModelAndView handleTypeMismatch(MethodArgumentTypeMismatchException e) {
        ModelAndView mav = new ModelAndView(ViewNames.VIEW_GENERAL_ERROR);
        String name = e.getName(); // The param name (e.g., "minPrice")
        String type = (e.getRequiredType() != null) ? e.getRequiredType().getSimpleName() : "Unknown"; // The expected type (e.g., "BigDecimal")
        Object value = e.getValue(); // The bad value (e.g., "abc")

        mav.addObject("errorMessage", String.format("Invalid value '%s' for parameter '%s'. Should be of type %s.", value, name, type));
        return mav;
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ModelAndView handleMaxSizeException(MaxUploadSizeExceededException e) {
        ModelAndView mav = new ModelAndView(ViewNames.VIEW_GENERAL_ERROR);
        mav.addObject("errorMessage", "File is too large! Please upload an image smaller than 10MB.");
        return mav;
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ModelAndView handleDatabaseConstraintError(DataIntegrityViolationException e) {
        ModelAndView mav = new ModelAndView(ViewNames.VIEW_GENERAL_ERROR);
        // Friendly message instead of technical jargon
        mav.addObject("errorMessage", "Cannot delete this item because it is referenced by other records (e.g., Orders or Carts).");
        return mav;
    }

    // Handle general errors
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handleGeneralError(Exception e) {
        ModelAndView mav = new ModelAndView(ViewNames.VIEW_GENERAL_ERROR);
        mav.addObject("errorMessage", "An unexpected error occurred: " + e.getMessage());
        return mav;
    }
}