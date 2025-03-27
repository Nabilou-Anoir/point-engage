<template>
  <div class="notifications-container">
    <!-- En-tête -->
    <div class="header">
      <div class="header-content">
        <h1>Notifications</h1>
        <button class="mark-read-button" @click="markAllAsRead">Marquer comme lu</button>
      </div>
    </div>

    <!-- Liste des notifications récentes -->
    <div class="notifications-list">
      <div
        v-for="(notification, index) in notifications"
        :key="index"
        class="notification-item"
        :class="{ unread: !notification.read }"
        @click="markAsRead(notification)"
      >
        <div class="notification-icon">
          <i class="fas fa-bell"></i>
        </div>
        <div class="notification-content">
          <h3>{{ notification.title }}</h3>
          <p>{{ notification.message }}</p>
          <span class="notification-time">{{ notification.time }}</span>
        </div>
        <div v-if="!notification.read" class="notification-badge">Nouveau</div>
      </div>
    </div>

    <!-- Section des notifications précédentes -->
    <div class="previous-notifications">
      <div class="previous-notifications-content">
        <h2>Notifications précédentes</h2>
        <button class="close-button" @click="closeNotifications">Fermer</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { NOTIFICATIONS_DATA } from '../../constante/notifconstants'; // Importer les constantes

// Utiliser les notifications simulées
const notifications = ref(NOTIFICATIONS_DATA);

// Marquer une notification comme lue
const markAsRead = (notification) => {
  notification.read = true;
};

// Marquer toutes les notifications comme lues
const markAllAsRead = () => {
  notifications.value.forEach((notification) => {
    notification.read = true;
  });
};

// Fermer les notifications
const closeNotifications = () => {
  console.log('Fermer les notifications');
};
</script>


<style scoped src="./NotifView.css"></style>