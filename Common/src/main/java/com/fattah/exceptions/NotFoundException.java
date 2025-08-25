package com.fattah.exceptions;

public class NotFoundException extends Exception {
    public NotFoundException() {
      super("Data Not Found!");
    }
}
