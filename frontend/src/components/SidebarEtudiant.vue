<!--src/components/Sidebar.vue &ndash;&gt;-->
<template>
  <aside class="sidebar">
    <!-- Zone utilisateur sans photo -->
    <div class="user-info">
      <h3 class="user-name">Bonjour</h3>
      <p class="user-promo">Bienvenue sur votre session</p>
    </div>

    <!-- Menu latéral -->
    <nav class="menu">
      <router-link to="/saisir-fiche" class="menu-item">
        Saisir une fiche
      </router-link>
      <router-link to="/historique-fiches" class="menu-item">
        Consulter l'historique des fiches
      </router-link>
    </nav>
  </aside>
</template>

<script setup>
</script>

<style scoped>
.sidebar {
  width: 250px;
  background-color: #f4f4f4;
  border-right: 1px solid #ddd;
  display: flex;
  flex-direction: column;
  padding: 20px;
  min-height: 100vh;
}

/* Zone utilisateur */
.user-info {
  text-align: center;
  margin-bottom: 20px;
}

.user-name {
  margin: 0;
  font-size: 1rem;
  font-weight: bold;
}

.user-promo {
  margin: 0;
  font-size: 0.9rem;
  color: #666;
}

/* Menu latéral */
.menu {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.menu-item {
  padding: 10px;
  background-color: #fff;
  border-radius: 4px;
  text-decoration: none;
  color: #333;
  font-weight: 500;
  transition: background-color 0.2s;
}

.menu-item:hover {
  background-color: #e2e8f0;
}
</style>
