<template>
  <div class="profile-page">
    <div class="top-image">
      <img src="@/Images/headerProfile.png" alt="Header Image" />
    </div>
    <div class="main-container">
      <div class="profile-section">
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
        <div class="separator"></div>
        <div class="profile-content">
          <div v-if="activeTab === 'profile'" class="profile-form">
            <h2>Votre profil : {{ profile.firstName }} {{ profile.lastName }}</h2>
            <p><strong>Email :</strong> {{ profile.email }}</p>
            <p><strong>Promotion :</strong> {{ profile.promotion }}</p>
            <div v-if="!isEditing" class="profile-actions-row">
              <button @click="enableEdit" class="action-btn">
                <i class="fas fa-edit"></i> Modifier
              </button>
            </div>
            <div v-else class="edit-form">
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
        id: null,
        firstName: "",
        lastName: "",
        email: "",
        promotion: "",
      },
      editableProfile: {
        promotion: ""
      }
    };
  },
  created() {
    const storedUser = sessionStorage.getItem("loggedInUser");
    if (storedUser) {
      const user = JSON.parse(storedUser);
      const email = user.email || user.etudiant?.email || "";
      this.profile.email = email;
      fetch(`/api/etudiants/byEmail?email=${encodeURIComponent(email)}`)
        .then(res => res.json())
        .then(data => {
          this.profile.id = data.idEtudiant;
          this.profile.firstName = data.prenom;
          this.profile.lastName = data.nom;
          this.profile.promotion = data.promotion;
        })
        .catch(err => {
          console.error("Erreur lors de la récupération du profil:", err);
        });
    }
  },
  methods: {
    enableEdit() {
      this.editableProfile.promotion = this.profile.promotion;
      this.isEditing = true;
    },
    cancelEdit() {
      this.isEditing = false;
    },
    saveProfile() {
      const payload = {
        idEtudiant: this.profile.id,
        nom: this.profile.lastName,
        prenom: this.profile.firstName,
        email: this.profile.email,
        promotion: this.editableProfile.promotion
      };
      fetch(`/api/etudiants/${this.profile.id}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify(payload)
      })
        .then(res => {
          if (!res.ok) throw new Error("Erreur de mise à jour");
          return res.json();
        })
        .then(data => {
          this.profile.promotion = data.promotion;
          this.isEditing = false;
          alert("✅ Promotion mise à jour avec succès !");
        })
        .catch(err => {
          console.error("Erreur de mise à jour:", err);
          alert("❌ Erreur lors de la mise à jour");
        });
    }
  }
};
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap');
@import url("https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css");
.profile-page { font-family: "Poppins", sans-serif; min-height: 100vh; background-color: #f5f7fa; }
.top-image { width: 100%; height: 200px; overflow: hidden; box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1); }
.top-image img { width: 100%; height: 100%; object-fit: cover; }
.main-container { display: flex; flex: 1; margin-top: 20px; padding: 0 20px; }
.profile-section { flex: 1; padding: 40px 20px; background-color: #ffffff; border-radius: 12px; box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1); display: flex; gap: 20px; }
.action-buttons { display: flex; flex-direction: column; gap: 10px; width: 200px; }
.action-buttons button { background-color: #f8f8f9; color: #777; border: none; padding: 12px 24px; border-radius: 8px; cursor: pointer; font-size: 14px; transition: background-color 0.3s ease; text-align: left; display: flex; align-items: center; gap: 10px; }
.action-buttons button:hover { background-color: #5a3ccf; color: #fff; }
.action-buttons button.active { background-color: #7d64d4; color: #fff; }
.separator { width: 1px; background-color: #ddd; margin: 0 20px; }
.profile-content { flex: 1; }
.profile-form { display: flex; flex-direction: column; gap: 20px; }
.profile-actions-row { display: flex; gap: 10px; margin-top: 10px; }
.edit-form { display: flex; flex-direction: column; gap: 20px; }
.form-group { display: flex; flex-direction: column; gap: 8px; }
label { font-size: 14px; color: #333; font-weight: 500; }
.form-input { padding: 12px; border: 1px solid #ddd; border-radius: 8px; font-size: 14px; transition: border-color 0.3s ease, box-shadow 0.3s ease; }
.form-input:focus { border-color: #6b48ff; box-shadow: 0 0 8px rgba(107, 72, 255, 0.2); outline: none; }
.save-btn { background-color: #ED6962; color: white; border: none; padding: 12px 24px; border-radius: 8px; cursor: pointer; font-size: 14px; transition: background-color 0.3s ease; }
.save-btn:hover { background-color: rgb(127, 100, 247); }
.action-btn { background-color: #f8f8f9; color: #777; border: none; padding: 12px 24px; border-radius: 8px; cursor: pointer; font-size: 14px; transition: background-color 0.3s ease; display: flex; align-items: center; gap: 10px; }
.action-btn:hover { background-color: #5a3ccf; color: #fff; }
.password-section { max-width: 600px; margin: 0 auto; padding: 20px; }
h2 { font-size: 24px; color: #333; margin-bottom: 20px; display: flex; align-items: center; gap: 10px; }
p { font-size: 14px; color: #777; }
</style>
