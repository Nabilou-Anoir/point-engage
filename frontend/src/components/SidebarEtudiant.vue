<template>
  <aside class="sidebar">
    <!-- Zone utilisateur -->
    <div class="user-info" @click="navigateToDashboard">
      <div class="profile-pic">
        <img :src="userData.profileImage" alt="Profile" v-if="userData.profileImage" />
        <span v-else>{{ userInitials }}</span>
      </div>
      <div class="user-details">
        <h3 class="user-name">{{ fullName }}</h3>
        <p class="user-email" @click.stop="navigateToDashboard">{{ userData.email }}</p>
      </div>
    </div>

    <!-- Menu latéral spécifique à l'étudiant -->
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
import { ref, computed, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';

const route = useRoute();
const router = useRouter();

const userData = ref({
  nom: '',
  prenom: '',
  email: '',
  profileImage: '',
  role: ''
});

onMounted(() => {
  const storedUser = sessionStorage.getItem('loggedInUser');
  if (storedUser) {
    const user = JSON.parse(storedUser);
    if (user.etudiant) {
      userData.value = {
        nom: user.etudiant.nom,
        prenom: user.etudiant.prenom,
        email: user.etudiant.email,
        profileImage: user.profileImage || '',
        role: 'etudiant'
      };
    } else {
      userData.value = {
        nom: user.nom || '',
        prenom: user.prenom || '',
        email: user.email || '',
        profileImage: user.profileImage || '',
        role: user.role || ''
      };
    }
  }
});

const fullName = computed(() => {
  return `${userData.value.nom} ${userData.value.prenom}`.trim();
});

const userInitials = computed(() => {
  const { nom, prenom } = userData.value;
  return `${prenom?.[0] || ''}${nom?.[0] || ''}`.toUpperCase();
});

const menuItems = ref([
  { path: '/etudiant/saisir-fiche', text: 'Saisir une fiche', icon: 'fas fa-file-upload' },
  { path: '/etudiant/historique-fiches', text: 'Historique des fiches', icon: 'fas fa-history' },
  { path: '/etudiant/mes-demandes', text: 'Mes demandes', icon: 'fas fa-demande' }

]);
const isActive = (path) => route.path === path;
const navigateToDashboard = () => router.push('/etudiant/accueil');
</script>

<style scoped>
/* src/assets/styles/sidebar.css */
.sidebar {
  width: 220px;
  height: 100vh;
  background-color: #6A3FA0;
  display: flex;
  flex-direction: column;
  padding: 20px;
  color: white;
  top: 16%;
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
