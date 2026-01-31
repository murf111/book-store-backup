package com.epam.rd.autocode.spring.project.controller.advice;

import com.epam.rd.autocode.spring.project.exception.AlreadyExistException;
import com.epam.rd.autocode.spring.project.exception.NotFoundException;
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
        mav.addObject("errorMessage", e.getMessage());
        mav.setViewName("error/404"); // You need to create templates/error/404.html
        return mav;
    }

    @ExceptionHandler(AlreadyExistException.class)
    public ModelAndView handleAlreadyExist(AlreadyExistException e) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("errorMessage", e.getMessage());
        mav.setViewName("error/500"); // Create templates/error/500.html
        return mav;
    }

    // Handle general errors
    @ExceptionHandler(Exception.class)
    public ModelAndView handleGeneralError(Exception e) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("errorMessage", "An unexpected error occurred: " + e.getMessage());
        mav.setViewName("error/500"); // Create templates/error/500.html
        return mav;
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ModelAndView handleMaxSizeException(MaxUploadSizeExceededException exc) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("errorMessage", "File is too large! Please upload an image smaller than 10MB.");
        mav.setViewName("error/500");
        return mav;
    }
}