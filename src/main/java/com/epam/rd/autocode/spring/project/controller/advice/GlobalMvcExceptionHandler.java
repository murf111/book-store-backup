package com.epam.rd.autocode.spring.project.controller.advice;

import com.epam.rd.autocode.spring.project.exception.AlreadyExistException;
import com.epam.rd.autocode.spring.project.exception.InsufficientFundsException;
import com.epam.rd.autocode.spring.project.exception.NotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalMvcExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleNotFound(NotFoundException e) {
        ModelAndView mav = new ModelAndView("error/404");
        mav.addObject("errorMessage", e.getMessage());
        return mav;
    }

    @ExceptionHandler(AlreadyExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ModelAndView handleAlreadyExist(AlreadyExistException e) {
        ModelAndView mav = new ModelAndView("error/error");
        mav.addObject("errorMessage","Action Failed: " + e.getMessage());
        return mav;
    }

    @ExceptionHandler(InsufficientFundsException.class)
    @ResponseStatus(HttpStatus.PAYMENT_REQUIRED)
    public ModelAndView handleInsufficientFunds(InsufficientFundsException e) {
        ModelAndView mav = new ModelAndView("error/error");
        mav.addObject("errorMessage", "Transaction Failed: " + e.getMessage());
        return mav;
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ModelAndView handleMaxSizeException(MaxUploadSizeExceededException e) {
        ModelAndView mav = new ModelAndView("error/error");
        mav.addObject("errorMessage", "File is too large! Please upload an image smaller than 10MB.");
        return mav;
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ModelAndView handleDatabaseConstraintError(DataIntegrityViolationException e) {
        ModelAndView mav = new ModelAndView("error/error");
        // Friendly message instead of technical jargon
        mav.addObject("errorMessage", "Cannot delete this item because it is referenced by other records (e.g., Orders or Carts).");
        return mav;
    }

    // Handle general errors
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handleGeneralError(Exception e) {
        ModelAndView mav = new ModelAndView("error/error");
        mav.addObject("errorMessage", "An unexpected error occurred: " + e.getMessage());
        return mav;
    }
}