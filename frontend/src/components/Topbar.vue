<template>
  <nav class="topbar">
    <!-- Section gauche : Logo -->
    <div class="left-section">
      <img src="../Images/logo.png" alt="Logo" class="logo" />
    </div>

    <!-- Section centrale : Liens de navigation -->
    <div class="center-section">
      <!-- Liens communs -->
      <router-link to="/directeur/accueil" class="nav-link" :class="{ active: isActive('/') }">
        <i class="fas fa-home"></i>
        <span>Accueil</span>
      </router-link>

      <!-- Liens spécifiques au directeur -->
      <template v-if="userRole === 'directeur'">
        <router-link to="/directeur/notifications" class="nav-link" :class="{ active: isActive('/directeur/notifications') }">
          <i class="fas fa-bell"></i>
          <span>Notifications</span>
          <div class="notif-icon" v-if="notifCount > 0">
            <span class="notif-count">{{ notifCount }}</span>
          </div>
        </router-link>
        <router-link to="/directeur/profil" class="nav-link" :class="{ active: isActive('/directeur/profil') }"> 
          <i class="fas fa-user"></i>
          <span>Profil</span>
        </router-link>
      </template>

      <!-- Liens spécifiques à l'étudiant -->
      <template v-if="userRole === 'etudiant'">
        <router-link to="/etudiant/notifications" class="nav-link" :class="{ active: isActive('/etudiant/notifications') }">
          <i class="fas fa-bell"></i>
          <span>Notifications</span>
          <div class="notif-icon" v-if="notifCount > 0">
            <span class="notif-count">{{ notifCount }}</span>
          </div>
        </router-link>
        <router-link to="/etudiant/profil" class="nav-link" :class="{ active: isActive('/etudiant/profil') }"> 
          <i class="fas fa-user"></i>
          <span>Profil</span>
        </router-link>
      </template>

      <!-- Liens spécifiques au référent -->
      <template v-if="userRole === 'referent'">
        <router-link to="/referent/notifications" class="nav-link" :class="{ active: isActive('/referent/notifications') }">
          <i class="fas fa-bell"></i>
          <span>Notifications</span>
          <div class="notif-icon" v-if="notifCount > 0">
            <span class="notif-count">{{ notifCount }}</span>
          </div>
        </router-link>
        <router-link to="/referent/profil" class="nav-link" :class="{ active: isActive('/referent/profil') }"> 
          <i class="fas fa-user"></i>
          <span>Profil</span>
        </router-link>
        <router-link to="/referent/historique" class="nav-link" :class="{ active: isActive('/referent/historique') }">
          <i class="fas fa-history"></i>
          <span>Historique</span>
        </router-link>
      </template>

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
import { ref, computed, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';

// Simuler un nombre de notifications
const notifCount = ref(3);

// Données de l'utilisateur
const userData = ref({
  name: 'Adrien Defossez',
  email: 'adrien.defossez@univ-jfc.fr',
  profileImage: '',
});

// Popup déconnexion
const showLogoutConfirm = ref(false);
const toggleDropdown = () => {
  showLogoutConfirm.value = !showLogoutConfirm.value;
};

// Redirection
const route = useRoute();
const router = useRouter();

const isActive = (path) => route.path === path;

// Rôle utilisateur selon l’URL
const userRole = computed(() => {
  return route.path.startsWith('/directeur') ? 'directeur' :
         route.path.startsWith('/scolarite') ? 'scolarite' :
         route.path.startsWith('/referent') ? 'referent' :
         'etudiant';
});

// Mise à jour du userData selon le rôle
watch(() => userRole.value, (newRole) => {
  if (newRole === 'etudiant') {
    userData.value = {
      name: 'Ines Gribaa',
      email: 'ines.gribaa@univ-jfc.fr',
      profileImage: '',
    };
  } else if (newRole === 'referent') {
    userData.value = {
      name: 'Référent',
      email: 'referent@univ-jfc.fr',
      profileImage: '',
    };
  } else {
    userData.value = {
      name: 'Adrien Defossez',
      email: 'adrien.defossez@univ-jfc.fr',
      profileImage: '',
    };
  }
}, { immediate: true });

const logout = () => {
  // Logique de déconnexion ici (ex : suppression token, redirection, etc.)
  console.log('Déconnecté');
  router.push('/');
};
</script>

<style scoped>
.topbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background-color: #6A3FA0;
  padding: 15px 30px;
  color: #fff;
  box-shadow: 0px 4px 4px rgba(0, 0, 0, 0.2);
  height: 80px;
  width: 100%;
  position: fixed;
  top: 0;
  left: 0;
  z-index: 1000;
}

.left-section {
  display: flex;
  align-items: center;
}

.logo {
  height: 70px;
  width: auto;
}

.center-section {
  display: flex;
  justify-content: flex-end;
  flex-grow: 1;
  gap: 2rem;
  align-items: center;
  padding-right: 50px;
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
}

.nav-link:hover, .nav-link.active {
  background: rgba(255, 255, 255, 0.3);
  transform: translateY(-2px);
}

.notif-icon {
  width: 20px;
  height: 20px;
  background: #D7443A;
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
  0% { transform: scale(1); }
  50% { transform: scale(1.1); }
  100% { transform: scale(1); }
}

.logout-container {
  position: relative;
  cursor: pointer;
}

.popup-overlay {
  position: absolute;
  top: 100%;
  right: 0;
  background-color: rgba(0, 0, 0, 0.6);
  width: 250px;
  padding: 10px;
  border-radius: 8px;
  z-index: 1001;
}

.popup-box {
  background-color: white;
  color: black;
  border-radius: 8px;
  padding: 16px;
  text-align: center;
}

.popup-icon {
  font-size: 2rem;
  color: #6A3FA0;
  margin-bottom: 10px;
}

.popup-message {
  margin-bottom: 12px;
}

.popup-buttons {
  display: flex;
  justify-content: space-around;
  gap: 10px;
}

.btn-cancel,
.btn-confirm {
  padding: 6px 12px;
  border: none;
  border-radius: 5px;
  font-weight: 500;
  cursor: pointer;
}

.btn-cancel {
  background-color: #ccc;
}

.btn-confirm {
  background-color: #D7443A;
  color: white;
}

/* Icônes Font Awesome */
.fas {
  font-size: 1.2rem;
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
