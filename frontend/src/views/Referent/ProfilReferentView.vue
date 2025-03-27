<template>
    <div class="profile-page">
      <!-- Image en haut de l'écran -->
      <div class="top-image">
        <img src="@/Images/headerProfile.png" alt="Header Image" />
      </div>
  
      <!-- Conteneur principal pour la sidebar et le contenu -->
      <div class="main-container">
        <!-- Profile Section -->
        <div class="profile-section">
          <!-- Boutons à droite -->
          <div class="action-buttons">
            <button
              @click="activeTab = 'profile'"
              :class="{ active: activeTab === 'profile' }"
            >
              <i class="fas fa-user"></i> Profil
            </button>
            <button
              @click="activeTab = 'notifications'"
              :class="{ active: activeTab === 'notifications' }"
            >
              <i class="fas fa-bell"></i> Notifications
            </button>
            <button @click="openPasswordPopup">
              <i class="fas fa-lock"></i> Mot de passe
            </button>
          </div>
  
          <!-- Ligne de séparation -->
          <div class="separator"></div>
  
          <!-- Contenu principal -->
          <div class="profile-content">
            <!-- Profile Picture and Role -->
            <div class="profile-header">
              <div class="profile-pic-container">
                <img
                  src="@/Images/headerProfile.png"
                  alt="Profile Picture"
                  class="profile-pic"
                  v-if="profile.image"
                />
                <div v-else class="profile-pic-placeholder">
                  {{ profileInitials }}
                </div>
                <span class="role">Référent</span> <!-- Mettre à jour le rôle -->
              </div>
              <span class="notification-badge">8</span>
            </div>
  
            <!-- Profile Form -->
            <div v-if="activeTab === 'profile'" class="profile-form">
              <form @submit.prevent="saveProfile">
                <!-- Nom et Prénom sur la même ligne -->
                <div class="name-fields">
                  <div class="form-group">
                    <label for="lastName">Nom</label>
                    <input
                      type="text"
                      id="lastName"
                      v-model="profile.lastName"
                      required
                      class="form-input"
                    />
                  </div>
                  <div class="form-group">
                    <label for="firstName">Prénom</label>
                    <input
                      type="text"
                      id="firstName"
                      v-model="profile.firstName"
                      required
                      class="form-input"
                    />
                  </div>
                </div>
                <div class="form-group">
                  <label for="email">Email</label>
                  <input
                    type="email"
                    id="email"
                    v-model="profile.email"
                    required
                    class="form-input"
                  />
                </div>
                <p class="last-update">Dernière mise à jour : 1 août 2023</p>
                <button type="submit" class="save-btn">
                  <i class="fas fa-save"></i> Enregistrer
                </button>
              </form>
            </div>
  
            <!-- Notifications -->
            <div v-if="activeTab === 'notifications'" class="notifications-section">
              <h2><i class="fas fa-bell"></i> Notifications</h2>
              <p>Vous n'avez pas de nouvelles notifications.</p>
            </div>
          </div>
        </div>
      </div>
  
      <!-- Popup pour changer le mot de passe -->
      <PasswordPopup
        v-if="showPasswordPopup"
        :LockIcon="LockIcon"
        :password="password"
        @change-password="changePassword"
        @close-popup="closePasswordPopup"
      />
    </div>
  </template>
  
  <script>
  
  import { PROFILE_DATA_REFERENT, LOCK_ICON } from '../../constante/profileconstants';
  import PasswordPopup from "../../components/PasswordPopup.vue";
  
  export default {
    name: "ProfileView",
    data() {
  return {
    activeTab: "profile", 
    showPasswordPopup: false, 
    profile: PROFILE_DATA_REFERENT, 
    password: {
      new: "",
      confirm: "",
    },
    LockIcon: LOCK_ICON, 
  };
},
    components: {
      PasswordPopup,
    },
    computed: {
      // Initiales du profil
      profileInitials() {
        return (
          this.profile.firstName.charAt(0) + this.profile.lastName.charAt(0)
        ).toUpperCase();
      },
    },
    methods: {
      // Sauvegarder le profil
      saveProfile() {
        console.log("Profil mis à jour :", this.profile);
        alert("Profil mis à jour avec succès !");
      },
      // Changer le mot de passe
      changePassword() {
        if (this.password.new !== this.password.confirm) {
          alert("Les mots de passe ne correspondent pas.");
          return;
        }
        if (this.password.new.length < 8) {
          alert("Le mot de passe doit contenir au moins 8 caractères.");
          return;
        }
        console.log("Mot de passe changé :", this.password);
        this.password = { new: "", confirm: "" }; // Réinitialiser le formulaire
        alert("Mot de passe changé avec succès !");
        this.closePasswordPopup(); // Fermer la popup après la soumission
      },
      // Ouvrir la popup
      openPasswordPopup() {
        this.showPasswordPopup = true;
      },
      // Fermer la popup
      closePasswordPopup() {
        this.showPasswordPopup = false;
      },
      // Gérer l'upload d'image
      handleImageUpload(event) {
        const file = event.target.files[0];
        if (file) {
          const reader = new FileReader();
          reader.onload = (e) => {
            this.profile.image = e.target.result;
          };
          reader.readAsDataURL(file);
        }
      },
    },
  };
  </script>
  
  <style scoped src="./ProfileView.css"></style>