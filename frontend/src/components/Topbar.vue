<template>
  <nav class="topbar">
    <!-- Section gauche : Logo -->
    <div class="left-section">
      <img src="../Images/logo.png" alt="Logo" class="logo" />
    </div>

    <!-- Section centrale : Liens de navigation -->
    <div class="center-section">
      <!-- Liens communs -->
      <router-link to="/" class="nav-link" :class="{ active: isActive('/') }">
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
        <!-- Affichage dynamique de l'e-mail -->
        <span class="user-email">{{ userData.email }}</span>
        <!-- Menu déroulant (optionnel) -->
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
    </div>
  </nav>
</template>

<script setup>
import { ref, computed, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';

// Simuler un nombre de notifications
const notifCount = ref(3);

// Données de l'utilisateur
const userData = ref({
  name: 'Adrien Defossez', // Valeur par défaut
  email: 'adrien.defossez@univ-jfc.fr', // Valeur par défaut
  profileImage: '', // Optionnel
});

// Gestion du menu déroulant
const dropdownVisible = ref(false);
const toggleDropdown = () => {
  dropdownVisible.value = !dropdownVisible.value;
};

// Vérifier si un lien est actif
const route = useRoute();
const isActive = (path) => {
  return route.path === path;
};

// Récupérer le rôle de l'utilisateur
const userRole = computed(() => {
  // Supposons que le rôle est stocké dans la route ou dans un store
  return route.path.startsWith('/directeur') ? 'directeur' : 
         route.path.startsWith('/scolarite') ? 'scolarite' : 
         route.path.startsWith('/referent') ? 'referent' : 
         'etudiant';
});

// Mettre à jour userData en fonction du rôle
watch(
  () => userRole.value,
  (newRole) => {
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
  },
  { immediate: true } // Exécuter immédiatement au chargement
);

// Rediriger vers l'accueil approprié
const router = useRouter();
const navigateToHome = () => {
  if (userRole.value === 'etudiant') {
    router.push('/etudiant/accueil');
  } else if (userRole.value === 'referent') {
    router.push('/referent/accueil');
  } else {
    router.push('/directeur/accueil');
  }
};
</script>

<style scoped>
/* Styles globaux */
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

/* Icônes Font Awesome */
.fas {
  font-size: 1.2rem;
}
</style>