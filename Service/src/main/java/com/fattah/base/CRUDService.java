package com.fattah.base;

public interface CRUDService<Dto> extends CreateService<Dto>,ReadService<Dto>,UpdateService<Dto>,DeleteService<Dto> {
}
