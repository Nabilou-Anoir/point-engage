<template>
  <nav class="topbar">
    <div class="left-section">
      <img
        src="../Images/logo.png"
        alt="Logo"
        class="logo"
        @click="navigateToHome"
      />
    </div>

    <div class="right-section">
      <router-link
        to="/scolarite/accueil"
        class="nav-link"
        :class="{ active: isActive('/scolarite/accueil') }"
      >
        <i class="fas fa-home"></i>
        <span>Accueil</span>
      </router-link>

      <!-- Bouton Profil : affichage du profil de l'utilisateur connecté -->
      <router-link
        to="/scolarite/profil"
        class="nav-link"
        :class="{ active: isActive('/scolarite/profil') }"
      >
        <i class="fas fa-user"></i>
        <span>{{ userData.name || "Profil" }}</span>
      </router-link>

      <!-- Bouton Déconnexion avec popup -->
      <div class="nav-link logout-link" ref="logoutRef">
        <div @click="toggleDropdown">
          <i class="fas fa-sign-out-alt"></i>
          <span>Déconnexion</span>
        </div>
        <div v-if="dropdownVisible" class="dropdown-menu">
          <!-- Seul le bouton déconnexion est conservé -->
          <div class="dropdown-item" @click.stop="showLogoutConfirm = true">
            <i class="fas fa-sign-out-alt"></i>
            <span>Déconnexion</span>
          </div>
        </div>
      </div>
    </div>

    <!-- Pop-up de confirmation pour la déconnexion -->
    <div
      v-if="showLogoutConfirm"
      class="popup-overlay"
      @click.self="showLogoutConfirm = false"
    >
      <div class="popup-box">
        <div class="popup-icon">
          <i class="fas fa-question-circle"></i>
        </div>
        <p class="popup-message">
          Êtes-vous sûr de vouloir vous déconnecter ?
        </p>
        <div class="popup-buttons">
          <button class="btn-cancel" @click="showLogoutConfirm = false">
            Annuler
          </button>
          <button class="btn-confirm" @click="logout">
            Oui, déconnexion
          </button>
        </div>
      </div>
    </div>
  </nav>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount } from 'vue';
import { useRouter, useRoute } from 'vue-router';

const router = useRouter();
const route = useRoute();

const userData = ref({});

// Récupération des données de l'utilisateur connecté depuis le sessionStorage
onMounted(() => {
  const storedUser = sessionStorage.getItem('loggedInUser');
  if (storedUser) {
    userData.value = JSON.parse(storedUser);
  }
  document.addEventListener('click', handleOutsideClick);
});

onBeforeUnmount(() => {
  document.removeEventListener('click', handleOutsideClick);
});

// Gestion du dropdown de déconnexion
const dropdownVisible = ref(false);
const showLogoutConfirm = ref(false);
const logoutRef = ref(null);

const handleOutsideClick = (e) => {
  if (logoutRef.value && !logoutRef.value.contains(e.target)) {
    dropdownVisible.value = false;
  }
};

const isActive = (path) => route.path === path;
const toggleDropdown = () => {
  dropdownVisible.value = !dropdownVisible.value;
};

const navigateToHome = () => {
  router.push('/scolarite/accueil');
};

const logout = () => {
  sessionStorage.removeItem('loggedInUser');
  router.push('/login');
};
</script>

<style scoped>
/* =======================
   Barre de navigation
   ======================= */

.topbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background-color: #6a3fa0;
  padding: 0 30px;
  color: #fff;
  box-shadow: 0px 4px 4px rgba(0, 0, 0, 0.2);
  height: 80px;
  width: 100%;
  position: fixed;
  top: 0;
  left: 0;
  z-index: 1000;
  overflow: visible;
}

.left-section {
  display: flex;
  align-items: center;
}

.logo {
  height: 70px;
  width: auto;
  cursor: pointer;
}

.right-section {
  display: flex;
  align-items: center;
  gap: 1.5rem;
}

.nav-link {
  color: white;
  text-decoration: none;
  font-weight: 500;
  font-size: 1.1rem;
  padding: 10px 15px;
  border-radius: 5px;
  transition: background 0.3s, transform 0.2s;
  position: relative;
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
}

.nav-link:hover,
.nav-link.active {
  background: rgba(255, 255, 255, 0.3);
  transform: translateY(-2px);
}

/* =======================
   Dropdown de déconnexion
   ======================= */

.logout-link {
  position: relative;
}

.dropdown-menu {
  position: absolute;
  top: 110%;
  right: 0;
  background-color: #6a3fa0;
  border-radius: 6px;
  box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.2);
  padding: 10px;
  display: flex;
  flex-direction: column;
  gap: 8px;
  z-index: 1001;
  min-width: 160px;
}

.dropdown-item {
  color: white;
  text-decoration: none;
  font-weight: 500;
  font-size: 0.95rem;
  padding: 8px 12px;
  border-radius: 4px;
  transition: background 0.3s;
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
}

.dropdown-item:hover {
  background: rgba(255, 255, 255, 0.2);
}

/* =======================
   Popup de déconnexion
   ======================= */

.popup-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: rgba(0, 0, 0, 0.6);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 2000;
}

.popup-box {
  background: white;
  padding: 30px;
  border-radius: 20px;
  text-align: center;
  max-width: 400px;
  width: 90%;
  box-shadow: 0 15px 40px rgba(0, 0, 0, 0.2);
  animation: slideIn 0.3s ease-in-out;
}

@keyframes slideIn {
  from {
    transform: translateY(-20px);
    opacity: 0;
  }
  to {
    transform: translateY(0);
    opacity: 1;
  }
}

.popup-icon {
  font-size: 2.5rem;
  color: #6a3fa0;
  margin-bottom: 20px;
}

.popup-message {
  font-size: 1.2rem;
  color: #333;
  margin-bottom: 20px;
  line-height: 1.6;
}

.popup-buttons {
  display: flex;
  gap: 15px;
  justify-content: center;
}

.btn-confirm {
  background: linear-gradient(135deg, #7f56d9, #5f99ae);
  color: white;
  padding: 12px 24px;
  border-radius: 12px;
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  border: none;
  transition: all 0.3s ease;
}

.btn-cancel {
  background: linear-gradient(135deg, #ed6962, #c0392b);
  color: white;
  padding: 12px 24px;
  border-radius: 12px;
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  border: none;
  transition: all 0.3s ease;
}

.btn-confirm:hover,
.btn-cancel:hover {
  transform: translateY(-3px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.2);
}

.btn-confirm:active,
.btn-cancel:active {
  transform: translateY(0);
}
</style>
