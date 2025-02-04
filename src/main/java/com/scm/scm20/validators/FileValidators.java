package com.scm.scm20.validators;

import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FileValidators implements ConstraintValidator<ValidFile,MultipartFile > {

    private static final long MAX_FILE_SIZE = 1024*1024*2; //2MB



    @Override
    public boolean isValid(MultipartFile  file, ConstraintValidatorContext context) {
       

        if(file==null || file.isEmpty()){
           /*  context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("File cannot be empty").addConstraintViolation(); */
            return true;
        }

        //file size

        if(file.getSize() > MAX_FILE_SIZE){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("File should be less than 2 MB").addConstraintViolation();
            return false;
        }

        //resolution

       /*  */

        return true;
        


    }

}
