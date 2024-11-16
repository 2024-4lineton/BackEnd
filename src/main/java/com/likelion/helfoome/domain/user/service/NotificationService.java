package com.likelion.helfoome.domain.user.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.likelion.helfoome.domain.user.dto.NotificationList;
import com.likelion.helfoome.domain.user.entity.Notification;
import com.likelion.helfoome.domain.user.entity.User;
import com.likelion.helfoome.domain.user.repository.NotificationRepository;
import com.likelion.helfoome.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {

  private final NotificationRepository notificationRepository;
  private final UserRepository userRepository;

  // 주문시 알람-> PIN저장해야해서 걍 따로 뻄
  public void createNotification(User user, String PIN) {
    Notification notification = new Notification();
    notification.setUser(user);
    notification.setTitle("예약이 수락되었어요 🍀");
    notification.setContent("늦기 전에 가게를 방문해 상품 구매를 완료해 주세요.");
    notification.setPIN(PIN);

    notificationRepository.save(notification);
  }

  public void createNotification(User user, int type) {
    Notification notification = new Notification();
    notification.setUser(user);

    switch (type) {
      case 0:
        notification.setTitle("오늘 하루 헬푸미 성공 💛");
        notification.setContent("가게 사장님이 주문을 확정했어요! \n헬푸미 구매 횟수가 1회 증가 했어요.");
        break;
      case 1:
        notification.setTitle("주문이 취소되었어요..😭");
        notification.setContent("가게 사정으로 인해 주문이 취소되었어요.");
        break;
    }
    notificationRepository.save(notification);
  }

  public List<NotificationList> getNotices(String email) {
    List<Notification> notices = notificationRepository.findByUser_Email(email);
    List<NotificationList> response = new ArrayList<>();
    for (Notification notice : notices) {
      NotificationList notificationList = new NotificationList();
      notificationList.setTitle(notice.getTitle());
      notificationList.setContent(notice.getContent());
      if (notice.getPIN() != null) {
        notificationList.setPIN(notice.getPIN());
      }
      notificationList.setCreatedAt(notice.getCreatedDate());
      response.add(notificationList);
    }
    return response;
  }
}
