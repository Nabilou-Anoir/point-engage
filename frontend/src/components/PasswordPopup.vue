<template>
  <div class="password-popup-overlay">
    <div class="password-popup">
      <div class="popup-icon">
        <!-- Utiliser la variable LockIcon pour l'attribut src -->
        <img :src="LockIcon" alt="Lock Icon" />
      </div>
      <h2>Changer votre mot de passe</h2>
      <form @submit.prevent="changePassword">
        <div class="form-group">
          <label for="newPassword">Nouveau mot de passe</label>
          <input
            type="password"
            id="newPassword"
            v-model="password.new"
            required
            class="form-input"
            placeholder="nouveau mot de passe"
          />
        </div>
        <div class="form-group">
          <label for="confirmPassword">Confirmer</label>
          <input
            type="password"
            id="confirmPassword"
            v-model="password.confirm"
            required
            class="form-input"
            placeholder="confirmer votre mot de passe"
          />
        </div>
        <p class="password-requirements">
          Le mot de passe doit contenir au moins 8 caractères.
        </p>
        <div class="popup-buttons">
          <button type="button" @click="closePopup" class="cancel-btn">
            Annuler
          </button>
          <button type="submit" class="confirm-btn">
            Confirmer
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
// Importer l'image
import LockIcon from "@/Images/lock.png";

export default {
  props: {
    password: Object, // Le mot de passe est passé en tant que prop
  },
  data() {
    return {
      LockIcon: LockIcon, // Utiliser l'image importée
    };
  },
  methods: {
    changePassword() {
      this.$emit("change-password", this.password);
    },
    closePopup() {
      this.$emit("close-popup");
    },
  },
};
</script>


<style scoped>
.password-popup-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.password-popup {
  background-color: #ffffff;
  padding: 30px;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
  max-width: 400px;
  width: 100%;
}

.password-popup h2 {
  font-size: 24px;
  color: #333;
  margin-bottom: 20px;
  text-align: center;
}

.password-popup .form-group {
  margin-bottom: 20px;
}

.password-popup .form-input {
  width: 100%;
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 8px;
  font-size: 14px;
}

.password-popup .password-requirements {
  font-size: 12px;
  color: #777;
  text-align: center;
  margin-bottom: 20px;
}

.password-popup .popup-buttons {
  display: flex;
  justify-content: space-between;
  gap: 10px;
}

.password-popup .cancel-btn {
  background-color: #f8f8f9;
  color: #333;
  border: none;
  padding: 12px 24px;
  border-radius: 8px;
  cursor: pointer;
  font-size: 14px;
  transition: background-color 0.3s ease;
  flex: 1;
}

.password-popup .cancel-btn:hover {
  background-color: #e0e0e0;
}

.password-popup .confirm-btn {
  background-color: #ED6962;
  color: white;
  border: none;
  padding: 12px 24px;
  border-radius: 8px;
  cursor: pointer;
  font-size: 14px;
  transition: background-color 0.3s ease;
  flex: 1;
}

.password-popup .confirm-btn:hover {
  background-color: #5a3ccf;
}

.popup-icon {
  text-align: center;
  margin-bottom: 20px;
}

.popup-icon img {
  width: 50px;
  height: 50px;
}
</style>