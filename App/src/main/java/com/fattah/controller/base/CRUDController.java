package com.fattah.controller.base;

public interface CRUDController<Dto> extends CreateController<Dto>,UpdateController<Dto>,ReadController<Dto>,DeleteController<Dto>{
}
