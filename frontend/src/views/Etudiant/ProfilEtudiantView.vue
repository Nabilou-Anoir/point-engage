<template>
  <div class="profile-page">
    <!-- Image d'en-tête -->
    <div class="top-image">
      <img src="@/Images/headerProfile.png" alt="Header Image" />
    </div>

    <!-- Conteneur principal -->
    <div class="main-container">
      <div class="profile-section">
        <!-- Barre latérale d'action (Profil et Mot de passe) -->
        <div class="action-buttons">
          <button
            @click="activeTab = 'profile'"
            :class="{ active: activeTab === 'profile' }"
          >
            <i class="fas fa-user"></i> Profil
          </button>
          <button @click="activeTab = 'password'">
            <i class="fas fa-lock"></i> Mot de passe
          </button>
        </div>

        <!-- Séparateur -->
        <div class="separator"></div>

        <!-- Contenu principal -->
        <div class="profile-content">
          <!-- Section Profil -->
          <div v-if="activeTab === 'profile'" class="profile-form">
            <h2>Votre profil : {{ profile.firstName }} {{ profile.lastName }}</h2>
            <p><strong>Email :</strong> {{ profile.email }}</p>
            <p><strong>Promotion :</strong> {{ profile.promotion }}</p>

            <!-- Bouton Modifier (mode lecture) -->
            <div v-if="!isEditing" class="profile-actions-row">
              <button @click="enableEdit" class="action-btn">
                <i class="fas fa-edit"></i> Modifier
              </button>
            </div>

            <!-- Mode édition (seulement email et promotion modifiables) -->
            <div v-else class="edit-form">
              <div class="form-group">
                <label for="email">Email</label>
                <input
                  type="email"
                  id="email"
                  v-model="editableProfile.email"
                  class="form-input"
                  required
                />
              </div>
              <div class="form-group">
                <label for="promotion">Promotion</label>
                <input
                  type="text"
                  id="promotion"
                  v-model="editableProfile.promotion"
                  class="form-input"
                  required
                />
              </div>
              <div class="profile-actions-row">
                <button @click="saveProfile" class="save-btn">
                  <i class="fas fa-save"></i> Enregistrer
                </button>
                <button @click="cancelEdit" class="action-btn">
                  Annuler
                </button>
              </div>
            </div>
          </div>

          <!-- Section Mot de passe (inchangée) -->
          <div v-if="activeTab === 'password'" class="password-section">
            <h2><i class="fas fa-lock"></i> Changer de mot de passe</h2>
            <p>Fonctionnalité à venir...</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "ProfilEtudiantView",
  data() {
    return {
      activeTab: "profile",
      isEditing: false,
      profile: {
        firstName: "",
        lastName: "",
        email: "",
        promotion: "",
      },
      // Copie éditable pour l’email et la promotion
      editableProfile: {
        email: "",
        promotion: ""
      }
    };
  },
  created() {
    // Récupération des données de l'utilisateur connecté depuis le sessionStorage
    const storedUser = sessionStorage.getItem("loggedInUser");
    if (storedUser) {
      const user = JSON.parse(storedUser);
      // On suppose que l'objet utilisateur connecté contient un champ "etudiant"
      if (user.etudiant) {
        this.profile.firstName = user.etudiant.prenom;
        this.profile.lastName = user.etudiant.nom;
        this.profile.email = user.etudiant.email;
        this.profile.promotion = user.etudiant.promotion;
      } else {
        // Sinon, fallback : les données sont directement dans user
        this.profile.firstName = user.firstName || "";
        this.profile.lastName = user.lastName || "";
        this.profile.email = user.email || "";
        this.profile.promotion = user.promotion || "";
      }
    }
  },
  methods: {
    enableEdit() {
      // Pré-remplir la copie éditable
      this.editableProfile.email = this.profile.email;
      this.editableProfile.promotion = this.profile.promotion;
      this.isEditing = true;
    },
    cancelEdit() {
      this.isEditing = false;
    },
    saveProfile() {
      // Exemple : ici, vous pouvez appeler votre API pour mettre à jour le profil.
      // On simule la mise à jour localement.
      this.profile.email = this.editableProfile.email;
      this.profile.promotion = this.editableProfile.promotion;
      this.isEditing = false;
      alert("Profil mis à jour avec succès !");
      console.log("Profil mis à jour :", this.profile);
    }
  }
};
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap');
@import url("https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css");

.profile-page {
  font-family: "Poppins", sans-serif;
  min-height: 100vh;
  background-color: #f5f7fa;
}

.top-image {
  width: 100%;
  height: 200px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.top-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.main-container {
  display: flex;
  flex: 1;
  margin-top: 20px;
  padding: 0 20px;
}

.profile-section {
  flex: 1;
  padding: 40px 20px;
  background-color: #ffffff;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  display: flex;
  gap: 20px;
}

.action-buttons {
  display: flex;
  flex-direction: column;
  gap: 10px;
  width: 200px;
}

.action-buttons button {
  background-color: #f8f8f9;
  color: #777;
  border: none;
  padding: 12px 24px;
  border-radius: 8px;
  cursor: pointer;
  font-size: 14px;
  transition: background-color 0.3s ease;
  text-align: left;
  display: flex;
  align-items: center;
  gap: 10px;
}

.action-buttons button:hover {
  background-color: #5a3ccf;
  color: #fff;
}

.action-buttons button.active {
  background-color: #7d64d4;
  color: #fff;
}

.separator {
  width: 1px;
  background-color: #ddd;
  margin: 0 20px;
}

.profile-content {
  flex: 1;
}

.profile-form {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

/* Boutons en bas du profil (Modifier, Enregistrer, Annuler) */
.profile-actions-row {
  display: flex;
  gap: 10px;
  margin-top: 10px;
}

.edit-form {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

label {
  font-size: 14px;
  color: #333;
  font-weight: 500;
}

.form-input {
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 8px;
  font-size: 14px;
  transition: border-color 0.3s ease, box-shadow 0.3s ease;
}

.form-input:focus {
  border-color: #6b48ff;
  box-shadow: 0 0 8px rgba(107, 72, 255, 0.2);
  outline: none;
}

/* Bouton "Enregistrer" */
.save-btn {
  background-color: #ED6962;
  color: white;
  border: none;
  padding: 12px 24px;
  border-radius: 8px;
  cursor: pointer;
  font-size: 14px;
  transition: background-color 0.3s ease;
}

.save-btn:hover {
  background-color: rgb(127, 100, 247);
}

/* Boutons "Modifier" et "Annuler" - même style */
.action-btn {
  background-color: #f8f8f9;
  color: #777;
  border: none;
  padding: 12px 24px;
  border-radius: 8px;
  cursor: pointer;
  font-size: 14px;
  transition: background-color 0.3s ease;
  display: flex;
  align-items: center;
  gap: 10px;
}

.action-btn:hover {
  background-color: #5a3ccf;
  color: #fff;
}

/* Section mot de passe */
.password-section {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px;
}

h2 {
  font-size: 24px;
  color: #333;
  margin-bottom: 20px;
  display: flex;
  align-items: center;
  gap: 10px;
}

p {
  font-size: 14px;
  color: #777;
}
</style>
