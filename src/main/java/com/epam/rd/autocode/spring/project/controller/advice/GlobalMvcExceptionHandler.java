package com.epam.rd.autocode.spring.project.controller.advice;

import com.epam.rd.autocode.spring.project.exception.AlreadyExistException;
import com.epam.rd.autocode.spring.project.exception.NotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalMvcExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ModelAndView handleNotFound(NotFoundException e) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("errorMessage", "Not found. Try again");
        mav.setViewName("error/404");
        return mav;
    }

    @ExceptionHandler(AlreadyExistException.class)
    public ModelAndView handleAlreadyExist(AlreadyExistException e) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("errorMessage", "Already exist. Try again");
        mav.setViewName("error/500");
        return mav;
    }

    // Handle general errors
    @ExceptionHandler(Exception.class)
    public ModelAndView handleGeneralError(Exception e) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("errorMessage", "An unexpected error occurred. Please contact support.");
        mav.setViewName("error/500");
        return mav;
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ModelAndView handleMaxSizeException(MaxUploadSizeExceededException exc) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("errorMessage", "File is too large! Please upload an image smaller than 10MB.");
        mav.setViewName("error/500");
        return mav;
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ModelAndView handleDatabaseConstraintError(DataIntegrityViolationException e) {
        ModelAndView mav = new ModelAndView();
        // Friendly message instead of technical jargon
        mav.addObject("errorMessage", "Cannot delete this item because it is referenced by other records (e.g., Orders or Carts).");
        mav.setViewName("error/500");
        return mav;
    }
}