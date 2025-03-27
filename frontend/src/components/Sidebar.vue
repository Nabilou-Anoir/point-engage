<template>
  <aside class="sidebar">
    <!-- Zone utilisateur -->
    <div class="user-info" @click="navigateToProfile">
      <div class="profile-pic">
        <img :src="userData.profileImage" alt="Profile" v-if="userData.profileImage" />
        <span v-else>{{ userInitials }}</span>
      </div>
      <div class="user-details">
        <h3 class="user-name">{{ userData.name }}</h3>
        <p class="user-email">{{ userData.email }}</p>
      </div>
    </div>

    <!-- Menu latéral -->
    <nav class="menu">
      <router-link
        v-for="item in menuItems"
        :key="item.path"
        :to="item.path"
        class="menu-item"
        :class="{ active: isActive(item.path) }"
      >
        <i :class="item.icon" class="menu-icon"></i>
        <span class="menu-text">{{ item.text }}</span>
      </router-link>
    </nav>
  </aside>
</template>

<script setup>
import { ref, computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';

// Récupérer la route actuelle pour déterminer le rôle de l'utilisateur
const route = useRoute();
const userRole = computed(() => {
  return route.path.startsWith('/etudiant') ? 'etudiant' : 'directeur';
});

// Définir les données utilisateur en fonction du rôle
const userData = computed(() => {
  return userRole.value === 'directeur'
    ? { name: 'Adrien Defossez', email: 'adrien.defossez@univ-jfc.fr', profileImage: '' }
    : { name: 'Ines Gribaa', email: 'ines.gribaa@univ-jfc.fr', profileImage: '' };
});

// Calculer les initiales de l'utilisateur
const userInitials = computed(() => {
  const names = userData.value.name.split(' ');
  return names.map(name => name[0]).join('');
});

// Menu pour le directeur
const menuItemsDirecteur = ref([
  { path: '/directeur/sessions', text: 'Gérer les sessions', icon: 'fas fa-calendar-alt' },
  { path: '/directeur/referent', text: 'Envoyer les fiches aux référents', icon: 'fas fa-paper-plane' },
  { path: '/directeur/attribuer-points', text: 'Attribuer les points', icon: 'fas fa-star' },
  { path: '/directeur/historique', text: 'Historique', icon: 'fas fa-history' },
  { path: '/directeur/modifier-referentiel', text: 'Modifier les référentiels', icon: 'fas fa-users-cog' },
  { path: '/directeur/modifier-acces', text: 'Modifier les Accès', icon: 'fas fa-key' },
  { path: '/directeur/points-cumules', text: 'Points cumulés', icon: 'fas fa-chart-line' },
]);

// Menu pour l'étudiant (le chemin pour "Saisir une fiche" est corrigé ici)
const menuItemsEtudiant = ref([
  { path: '/saisir-fiche', text: 'Saisir une fiche', icon: 'fas fa-file-upload' },
  { path: '/etudiant/historique-fiches', text: 'Consulter l’historique des fiches', icon: 'fas fa-history' },
  { path: '/etudiant/proposer-activite', text: 'Proposer une activité hors référentiel', icon: 'fas fa-lightbulb' },
]);

// Sélectionner les éléments du menu selon le rôle
const menuItems = computed(() => {
  return userRole.value === 'directeur' ? menuItemsDirecteur.value : menuItemsEtudiant.value;
});

// Fonction pour vérifier si le lien est actif
const isActive = (path) => {
  return route.path === path;
};

// Navigation vers la vue Profil (pour le directeur ici)
const router = useRouter();
const navigateToProfile = () => {
  router.push({ path: '/directeur/profil' });
};
</script>

<style scoped>
.sidebar {
  width: 220px;
  height: 100vh;
  background-color: #6A3FA0;
  display: flex;
  flex-direction: column;
  padding: 20px;
  color: white;
  overflow-y: auto;
  scrollbar-width: thin;
  scrollbar-color: rgba(255, 255, 255, 0.5) transparent;
  transition: width 0.3s ease;
}

.sidebar::-webkit-scrollbar {
  width: 5px;
}

.sidebar::-webkit-scrollbar-thumb {
  background-color: rgba(255, 255, 255, 0.5);
  border-radius: 10px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 15px;
  margin-bottom: 30px;
  padding: 10px;
  border-radius: 8px;
  cursor: pointer;
  transition: background 0.3s ease;
  flex-wrap: wrap;
}

.user-info:hover {
  background-color: rgba(255, 255, 255, 0.1);
}

.profile-pic {
  width: 50px;
  height: 50px;
  background-color: #fff;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  color: #6A3FA0;
}

.profile-pic img {
  width: 100%;
  height: 100%;
  border-radius: 50%;
  object-fit: cover;
}

.user-details {
  display: flex;
  flex-direction: column;
  width: 100%;
}

.user-name {
  margin: 0;
  font-size: 1.1rem;
  font-weight: bold;
}

.user-email {
  margin: 0;
  font-size: 0.9rem;
  color: rgba(255, 255, 255, 0.8);
  text-overflow: ellipsis;
  overflow: hidden;
  white-space: nowrap;
  max-width: 160px;
}

.menu {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.menu-item {
  display: flex;
  align-items: center;
  gap: 15px;
  padding: 12px 15px;
  background-color: rgba(255, 255, 255, 0.1);
  border-radius: 6px;
  text-decoration: none;
  color: white;
  font-weight: 500;
  transition: background 0.3s ease, transform 0.2s ease;
}

.menu-item:hover {
  background-color: rgba(255, 255, 255, 0.2);
  transform: translateX(5px);
}

.menu-item.active {
  background-color: rgba(255, 255, 255, 0.3);
  font-weight: bold;
}

.menu-icon {
  font-size: 1.2rem;
}

.menu-text {
  font-size: 0.95rem;
}
</style>
