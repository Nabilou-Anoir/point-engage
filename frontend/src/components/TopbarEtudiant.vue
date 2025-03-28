<template>
  <nav class="topbar">
    <!-- Section gauche : Logo -->
    <div class="left-section">
      <img src="../Images/logo.png" alt="Logo" class="logo" @click="navigateToHome" />
    </div>

    <!-- Section centrale : Liens de navigation spécifiques à l'étudiant -->
    <div class="center-section">
      <router-link to="/etudiant/accueil" class="nav-link" :class="{ active: isActive('/etudiant/accueil') }">
        <i class="fas fa-home"></i>
        <span>Accueil</span>
      </router-link>
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
    </div>

    <!-- Zone de déconnexion avec menu déroulant -->
    <div class="logout-container" @click="toggleDropdown">
      <div class="nav-link logout-link">
        <i class="fas fa-sign-out-alt"></i>
        <span>Déconnexion</span>
      </div>
      <span class="user-email">{{ userData.email }}</span>
      <div v-if="dropdownVisible" class="dropdown-menu">
        <router-link to="/settings" class="dropdown-item">
          <i class="fas fa-cog"></i>
          <span>Paramètres</span>
        </router-link>
        <router-link to="/logout" class="dropdown-item">
          <i class="fas fa-sign-out-alt"></i>
          <span>Déconnexion</span>
        </router-link>
      </div>
    </div>
  </nav>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';

const router = useRouter();
const route = useRoute();

// Simulation d'un nombre de notifications
const notifCount = ref(3);

// Récupération des données utilisateur depuis le sessionStorage
const userData = ref({
  name: '',
  email: '',
  profileImage: '',
  role: ''
});
onMounted(() => {
  const storedUser = sessionStorage.getItem('loggedInUser');
  if (storedUser) {
    userData.value = JSON.parse(storedUser);
  }
});

// Calcul des initiales (si nécessaire)
const userInitials = computed(() => {
  if (!userData.value.name) return '';
  return userData.value.name.split(' ').map(n => n.charAt(0)).join('');
});

// Vérifier si un lien est actif
const isActive = (path) => route.path === path;

// Gestion du menu déroulant
const dropdownVisible = ref(false);
const toggleDropdown = () => {
  dropdownVisible.value = !dropdownVisible.value;
};

// Redirection vers la page Accueil (dashboard étudiant)
const navigateToHome = () => {
  router.push('/etudiant/accueil');
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
  cursor: pointer;
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

.nav-link:hover,
.nav-link.active {
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

.user-email {
  font-size: 0.65rem;
  color: rgba(255, 255, 255, 0.8);
  margin-top: -2px;
  margin-left: 15px;
}

.logout-container {
  position: relative;
  cursor: pointer;
}

.dropdown-menu {
  position: absolute;
  top: 100%;
  right: 0;
  background-color: #6A3FA0;
  border-radius: 6px;
  box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.2);
  padding: 10px;
  display: flex;
  flex-direction: column;
  gap: 8px;
  z-index: 1001;
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

.fas {
  font-size: 1.2rem;
}
</style>
