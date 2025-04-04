<template>
  <div class="profile-page">
    <div class="top-image">
      <img src="@/Images/headerProfile.png" alt="Header Image" />
    </div>

    <div class="main-container">
      <div class="profile-section">
        <div class="action-buttons">
          <button @click="activeTab = 'profile'" :class="{ active: activeTab === 'profile' }">
            <i class="fas fa-user"></i> Profil
          </button>
          <button @click="activeTab = 'notifications'" :class="{ active: activeTab === 'notifications' }">
            <i class="fas fa-bell"></i> Notifications
          </button>
          <button @click="openPasswordPopup">
            <i class="fas fa-lock"></i> Mot de passe
          </button>
        </div>

        <div class="separator"></div>

        <div class="profile-content">
          <div class="profile-header">
            <div class="profile-pic-container">
              <img v-if="profile.image" :src="profile.image" alt="Profile Picture" class="profile-pic" />
              <div v-else class="profile-pic-placeholder">
                {{ profileInitials }}
              </div>
              <span class="role">Directeur</span>
            </div>
            <span class="notification-badge">8</span>
          </div>

          <div v-if="activeTab === 'profile'" class="profile-form">
            <form @submit.prevent="saveProfile">
              <div class="name-fields">
                <div class="form-group">
                  <label for="lastName">Nom</label>
                  <input type="text" id="lastName" v-model="profile.lastName" required class="form-input" />
                </div>
                <div class="form-group">
                  <label for="firstName">Prénom</label>
                  <input type="text" id="firstName" v-model="profile.firstName" required class="form-input" />
                </div>
              </div>
              <div class="form-group">
                <label for="email">Email</label>
                <input type="email" id="email" v-model="profile.email" required class="form-input" />
              </div>
              <p class="last-update">Dernière mise à jour : {{ lastUpdated }}</p>
              <button type="submit" class="save-btn">
                <i class="fas fa-save"></i> Enregistrer
              </button>
            </form>
          </div>

          <div v-if="activeTab === 'notifications'" class="notifications-section">
            <h2><i class="fas fa-bell"></i> Notifications</h2>
            <p>Vous n'avez pas de nouvelles notifications.</p>
          </div>
        </div>
      </div>
    </div>

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
import PasswordPopup from "@/components/PasswordPopup.vue";

export default {
  name: "ProfileView",
  data() {
    return {
      activeTab: "profile",
      showPasswordPopup: false,
      profile: {
        firstName: "",
        lastName: "",
        email: "",
        image: ""
      },
      password: {
        new: "",
        confirm: "",
      },
      LockIcon: "fas fa-lock",
      lastUpdated: new Date().toLocaleDateString('fr-FR'),
    };
  },
  components: {
    PasswordPopup,
  },
  computed: {
    profileInitials() {
      return (
        this.profile.firstName.charAt(0) + this.profile.lastName.charAt(0)
      ).toUpperCase();
    },
  },
  created() {
    const storedUser = sessionStorage.getItem("loggedInUser");
    if (storedUser) {
      const user = JSON.parse(storedUser);
      const email = user.email || "";
      this.profile.email = email;
      fetch(`/api/utilisateurs/byEmail?email=${encodeURIComponent(email)}`)
        .then(res => res.json())
        .then(data => {
          this.profile.firstName = data.prenom;
          this.profile.lastName = data.nom;
          this.profile.email = data.email;
        })
        .catch(err => {
          console.error("Erreur lors de la récupération du profil:", err);
        });
    }
  },
  methods: {
    saveProfile() {
      const storedUser = JSON.parse(sessionStorage.getItem("loggedInUser"));
      const payload = {
        idUser: storedUser.idUser,
        nom: this.profile.lastName,
        prenom: this.profile.firstName,
        email: this.profile.email,
        username: storedUser.username,
        password: storedUser.password,
        role: storedUser.role,
      };

      fetch(`/api/utilisateurs/${storedUser.idUser}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload),
      })
        .then(res => {
          if (!res.ok) throw new Error("Erreur de mise à jour");
          return res.json();
        })
        .then(() => {
          alert("Profil mis à jour avec succès !");
        })
        .catch(err => {
          console.error("Erreur de mise à jour:", err);
          alert("Erreur lors de la mise à jour du profil");
        });
    },
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
      this.password = { new: "", confirm: "" };
      alert("Mot de passe changé avec succès !");
      this.closePasswordPopup();
    },
    openPasswordPopup() {
      this.showPasswordPopup = true;
    },
    closePasswordPopup() {
      this.showPasswordPopup = false;
    },
  },
};
</script>

<style scoped src="./ProfileView.css"></style>
