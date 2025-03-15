// src/router/index.js
import { createRouter, createWebHistory } from 'vue-router'

// Import des composants de vue
import RoleSelection from '@/views/RoleSelection.vue'
import SelectionEtudiantView from '@/views/EtudiantView/SelectionEtudiantView.vue'
import AccueilEtudiantView from '@/views/EtudiantView/AccueilEtudiantView.vue'
import NotificationsView from '@/views/EtudiantView/NotificationsView.vue'
import ProfilView from '@/views/EtudiantView/ProfilView.vue'
import SaisirFicheView from '@/views/EtudiantView/SaisirFicheView.vue'
import HistoriqueFichesView from '@/views/EtudiantView/HistoriqueFichesView.vue'
import DispositifView from '@/views/EtudiantView/DispositifView.vue'

const routes = [
  // Rediriger la racine vers la page de sélection de rôle
  { path: '/', redirect: '/role' },
  { path: '/role', component: RoleSelection },

  // Sélection d'étudiant avant d'accéder à l'accueil
  { path: '/selection-etudiant', component: SelectionEtudiantView },

  // Accueil étudiant (protégé par un garde de navigation)
  {
    path: '/etudiant',
    name: 'AccueilEtudiant',
    component: AccueilEtudiantView,
    beforeEnter: (to, from, next) => {
      const selectedEtudiant = sessionStorage.getItem('selectedEtudiant')
      if (!selectedEtudiant) {
        next('/selection-etudiant') // Redirige si aucun étudiant sélectionné
      } else {
        next() // Accès autorisé
      }
    }
  },

  { path: '/notifications', component: NotificationsView },
  { path: '/profil', component: ProfilView },
  { path: '/saisir-fiche', component: SaisirFicheView },
  { path: '/historique-fiches', component: HistoriqueFichesView },
  { path: '/dispositif', component: DispositifView }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
