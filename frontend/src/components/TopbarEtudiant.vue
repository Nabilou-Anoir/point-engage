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
        to="/etudiant/accueil"
        class="nav-link"
        :class="{ active: isActive('/etudiant/accueil') }"
      >
        <i class="fas fa-home"></i>
        <span>Accueil</span>
      </router-link>

      <router-link
        to="/etudiant/notifications"
        class="nav-link"
        :class="{ active: isActive('/etudiant/notifications') }"
      >
        <i class="fas fa-bell"></i>
        <span>Notifications</span>
        <div class="notif-icon" v-if="notifCount > 0">
          <span class="notif-count">{{ notifCount }}</span>
        </div>
      </router-link>

      <router-link
        to="/etudiant/profil"
        class="nav-link"
        :class="{ active: isActive('/etudiant/profil') }"
      >
        <i class="fas fa-user"></i>
        <span>Profil</span>
      </router-link>

      <!-- Déconnexion -->
      <div class="logout-container" @click="toggleDropdown">
        <div class="nav-link logout-link">
          <i class="fas fa-sign-out-alt"></i>
          <span>Déconnexion</span>
        </div>
        <!-- Menu déroulant (popup de confirmation) -->
        <div
          v-if="showLogoutConfirm"
          class="popup-overlay"
          @click.self="showLogoutConfirm = false">
          <div class="popup-box" @click.stop>
            <div class="popup-icon">
              <i class="fas fa-question-circle"></i>
            </div>
            <p class="popup-message">
              Êtes-vous sûr de vouloir vous déconnecter ?
            </p>
            <div class="popup-buttons">
              <button class="btn-cancel" @click.stop="showLogoutConfirm = false">
                Annuler
              </button>
              <button class="btn-confirm" @click="logout">
                Oui, déconnexion
              </button>
            </div>
          </div>
        </div>
      </div>
    </div> <!-- FIN DE center-section -->
  </nav>
</template>

<script setup>
import { ref, computed, onMounted, onBeforeUnmount } from 'vue';
import { useRoute, useRouter } from 'vue-router';

const router = useRouter();
const route = useRoute();

// Nombre de notifications (exemple)
const notifCount = ref(3);

// Gérer le dropdown
const dropdownVisible = ref(false);
const showLogoutConfirm = ref(false);
const logoutRef = ref(null);

const userData = ref({});

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

const handleOutsideClick = (e) => {
  if (logoutRef.value && !logoutRef.value.contains(e.target)) {
    dropdownVisible.value = false;
  }
};

const isActive = (path) => route.path === path;
const toggleDropdown = () => (dropdownVisible.value = !dropdownVisible.value);

const navigateToHome = () => {
  router.push('/etudiant/accueil');
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
  height: 80px;       /* Hauteur fixe. Ajuster si nécessaire */
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
   Dropdown
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
}

.dropdown-item:hover {
  background: rgba(255, 255, 255, 0.2);
}

/* =======================
   Icône de notification
   ======================= */

.notif-icon {
  width: 20px;
  height: 20px;
  background: #d7443a;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  font-weight: bold;
  color: white;
  position: absolute;
  top: -5px;
  right: -10px;
  animation: pulse 1.5s infinite;
}

@keyframes pulse {
  0% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.1);
  }
  100% {
    transform: scale(1);
  }
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

/* =======================
   Media queries
   ======================= */

/* Sur les écrans <= 768px, on réduit le padding,
   on autorise le wrap et on ajuste la taille du texte. */
@media (max-width: 768px) {
  .topbar {
    padding: 0 15px;
    height: auto; /* Laisse la hauteur s'adapter si nécessaire */
  }

  .right-section {
    gap: 0.5rem;
    flex-wrap: wrap; /* Permet aux liens de passer à la ligne si besoin */
  }

  .nav-link {
    font-size: 1rem;
    padding: 8px 10px;
  }

  .logo {
    height: 50px;
  }
}
</style>
