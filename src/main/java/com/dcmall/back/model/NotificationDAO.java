package com.dcmall.back.model;


import java.util.ArrayList;
import java.util.List;


public interface NotificationDAO {
    List<SelectNotificationDTO> notifications(int num);
}
