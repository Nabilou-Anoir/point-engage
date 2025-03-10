// src/router/index.js
import { createRouter, createWebHistory } from 'vue-router'

// Importez vos vues existantes
import AccueilView from '@/views/EtudiantView/AccueilView.vue'
import NotificationsView from '@/views/EtudiantView/NotificationsView.vue'
import ProfilView from '@/views/EtudiantView/ProfilView.vue'
import SaisirFicheView from '@/views/EtudiantView/SaisirFicheView.vue'
import HistoriqueFichesView from '@/views/EtudiantView/HistoriqueFichesView.vue'
import ActiviteHorsRefView from '@/views/EtudiantView/ActiviteHorsRefView.vue'

// Ajoutez l'import de votre nouvelle vue pour le dispositif
import DispositifView from '@/views/EtudiantView/DispositifView.vue'

const routes = [
  { path: '/', component: AccueilView },
  { path: '/notifications', component: NotificationsView },
  { path: '/profil', component: ProfilView },
  { path: '/saisir-fiche', component: SaisirFicheView },
  { path: '/historique-fiches', component: HistoriqueFichesView },
  { path: '/activite-hors-ref', component: ActiviteHorsRefView },
  // Nouvelle route pour le dispositif Ingénieur Engagé
  { path: '/dispositif', component: DispositifView }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
