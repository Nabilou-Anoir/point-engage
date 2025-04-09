<template>
  <div class="profile-page">
    <!-- Image en haut de l'écran -->
    <div class="top-image">
      <img src="@/Images/headerProfile.png" alt="Header Image" />
    </div>

    <!-- Conteneur principal pour le profil -->
    <div class="main-container">
      <div class="profile-section">
        <!-- Boutons d'action -->
        <div class="action-buttons">
          <button
            @click="activeTab = 'profile'"
            :class="{ active: activeTab === 'profile' }"
          >
            <i class="fas fa-user"></i> Votre profil : {{ profile.firstName }} {{ profile.lastName }}
          </button>
          <button @click="openPasswordPopup">
            <i class="fas fa-lock"></i> Mot de passe
          </button>
        </div>

        <!-- Ligne de séparation -->
        <div class="separator"></div>

        <!-- Contenu principal du profil -->
        <div class="profile-content">
          <!-- Section photo et rôle -->
          <div class="profile-header">
            <div class="profile-pic-container">
              <img
                v-if="profile.image"
                :src="profile.image"
                alt="Profile Picture"
                class="profile-pic"
              />
              <div v-else class="profile-pic-placeholder">
                {{ profileInitials }}
              </div>
              <span class="role">Référent</span>
            </div>
          </div>

          <!-- Formulaire du profil -->
          <div v-if="activeTab === 'profile'" class="profile-form">
            <form @submit.prevent="handleSave">
              <div class="form-group">
                <label for="lastName">Nom</label>
                <input
                  type="text"
                  id="lastName"
                  v-model="profile.lastName"
                  :disabled="!isEditing"
                  class="form-input"
                  required
                />
              </div>
              <div class="form-group">
                <label for="firstName">Prénom</label>
                <input
                  type="text"
                  id="firstName"
                  v-model="profile.firstName"
                  :disabled="!isEditing"
                  class="form-input"
                  required
                />
              </div>
              <div class="form-group">
                <label for="email">Email</label>
                <input
                  type="email"
                  id="email"
                  v-model="profile.email"
                  :disabled="!isEditing"
                  class="form-input"
                  required
                />
              </div>
              <p class="last-update">Dernière mise à jour : {{ lastUpdated }}</p>
              <div class="form-actions">
                <button
                  type="button"
                  v-if="!isEditing"
                  class="save-btn"
                  @click="enableEditing"
                >
                  <i class="fas fa-edit"></i> Modifier
                </button>
                <button
                  type="submit"
                  v-else
                  class="save-btn"
                >
                  <i class="fas fa-save"></i> Enregistrer
                </button>
                <button
                  v-if="isEditing"
                  type="button"
                  class="save-btn"
                  @click="cancelEditing"
                >
                  Annuler
                </button>
              </div>
            </form>
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
import PasswordPopup from "@/components/PasswordPopup.vue";

export default {
  name: "ProfileView",
  data() {
    return {
      activeTab: "profile",
      showPasswordPopup: false,
      isEditing: false,
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
      lastUpdated: new Date().toLocaleDateString("fr-FR")
    };
  },
  computed: {
    profileInitials() {
      if (!this.profile.firstName || !this.profile.lastName) return "";
      return (this.profile.firstName.charAt(0) + this.profile.lastName.charAt(0)).toUpperCase();
    }
  },
  created() {
    const storedUser = sessionStorage.getItem("loggedInUser");
    if (storedUser) {
      const user = JSON.parse(storedUser);
      const email = user.email || "";
      fetch(`/api/utilisateurs/byEmail?email=${encodeURIComponent(email)}`)
        .then(res => res.json())
        .then(data => {
          this.profile.firstName = data.prenom;
          this.profile.lastName = data.nom;
          this.profile.email = data.email;
          this.profile.image = data.image || "";
        })
        .catch(err => {
          console.error("Erreur lors de la récupération du profil:", err);
        });
    } else {
      console.log("Aucun utilisateur connecté trouvé dans sessionStorage.");
    }
  },
  methods: {
    enableEditing() {
      this.isEditing = true;
    },
    cancelEditing() {
      this.isEditing = false;
      const storedUser = sessionStorage.getItem("loggedInUser");
      if (storedUser) {
        const user = JSON.parse(storedUser);
        const email = user.email;
        fetch(`/api/utilisateurs/byEmail?email=${encodeURIComponent(email)}`)
          .then(res => res.json())
          .then(data => {
            this.profile.firstName = data.prenom;
            this.profile.lastName = data.nom;
            this.profile.email = data.email;
            this.profile.image = data.image || "";
          })
          .catch(err => console.error("Erreur lors de la récupération du profil:", err));
      }
    },
    handleSave() {
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
        body: JSON.stringify(payload)
      })
        .then(res => {
          if (!res.ok) throw new Error("Erreur de mise à jour");
          return res.json();
        })
        .then(() => {
          alert("Profil mis à jour avec succès !");
          storedUser.email = this.profile.email;
          sessionStorage.setItem("loggedInUser", JSON.stringify(storedUser));
          this.isEditing = false;
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
    }
  }
};
</script>

<style scoped src="./ProfileView.css"></style>
