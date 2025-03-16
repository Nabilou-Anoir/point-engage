import { createRouter, createWebHistory } from "vue-router";

// 📌 Import des vues générales
import RoleSelection from "@/views/RoleSelection.vue";
import AccueilView from "@/views/DirecteurView/views/AccueilView.vue";
import NotificationsView from "@/views/DirecteurView/views/NotificationsView.vue";
import ProfilView from "@/views/DirecteurView/views/ProfilView.vue";
import SessionsView from "@/views/DirecteurView/views/SessionsView.vue";
import ReferentView from "@/views/DirecteurView/views/ReferentView.vue";
import AttribuerPointsView from "@/views/DirecteurView/views/AttribuerPointsView.vue";
import ModifierAcces from "@/views/DirecteurView/views/ModifierAcces.vue";
import ModifierReferentielView from "@/views/DirecteurView/views/ModifierReferentielView.vue";
import HistoriqueView from "@/views/DirecteurView/views/HistoriqueView.vue";
import PointCommuleView from "@/views/DirecteurView/views/PointCommuleView.vue";


// 📌 Import des vues spécifiques aux étudiants
import SelectionEtudiantView from "@/views/EtudiantView/SelectionEtudiantView.vue";
import AccueilEtudiantView from "@/views/EtudiantView/AccueilEtudiantView.vue";
import SaisirFicheView from "@/views/EtudiantView/SaisirFicheView.vue";
import HistoriqueFichesView from "@/views/EtudiantView/HistoriqueFichesView.vue";
import DispositifView from "@/views/EtudiantView/DispositifView.vue";

// 📌 Définition des routes
const routes = [
  // Redirection vers la sélection de rôle
  { path: "/", redirect: "/role" },

  // Sélection du rôle
  { path: "/role", component: RoleSelection },

  // 📌 Routes générales accessibles par plusieurs rôles
  { path: "/accueil", name: "Accueil", component: AccueilView },
  { path: "/notifications", name: "Notifications", component: NotificationsView },
  { path: "/sessions", name: "Sessions", component: SessionsView },
  { path: "/profil", name: "Profil", component: ProfilView },
  { path: "/referent", name: "Referent", component: ReferentView },
  { path: "/attribuer-points", name: "AttribuerPoints", component: AttribuerPointsView },
  { path: "/modifier-acces", name: "ModifierAcces", component: ModifierAcces },
  { path: "/modifier-referentiel", name: "ModifierReferentiel", component: ModifierReferentielView },
  { path: "/historique", name: "Historique", component: HistoriqueView },
  { path: "/points-cumules", name: "PointsCumules", component: PointCommuleView },

  // 📌 Routes spécifiques aux étudiants
  { path: "/selection-etudiant", component: SelectionEtudiantView },
  {
    path: "/etudiant",
    name: "AccueilEtudiant",
    component: AccueilEtudiantView,
    beforeEnter: (to, from, next) => {
      const selectedEtudiant = sessionStorage.getItem("selectedEtudiant");
      if (!selectedEtudiant) {
        next("/selection-etudiant"); // Redirige si aucun étudiant sélectionné
      } else {
        next(); // Accès autorisé
      }
    },
  },
  { path: "/saisir-fiche", name: "SaisirFiche", component: SaisirFicheView },
  { path: "/historique-fiches", name: "HistoriqueFiches", component: HistoriqueFichesView },
  { path: "/dispositif", name: "Dispositif", component: DispositifView },
];

// 📌 Création du routeur Vue
const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
