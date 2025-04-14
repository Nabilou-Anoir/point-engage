import { createRouter, createWebHistory } from "vue-router";

import LoginView from "@/views/LoginView.vue";

import AccueilView from "@/views/Directeur/AccueilDirecteurView.vue";
import NotificationsView from "@/views/Directeur/NotificationsView.vue";
import SessionsView from "@/views/Directeur/SessionsView.vue";
import ProfilView from "@/views/Directeur/ProfilView.vue";
import ReferentView from "@/views/Directeur/ReferentView.vue";
import AttribuerPointsView from "@/views/Directeur/AttribuerPointsView.vue";
import ModifierAcces from "@/views/Directeur/ModifierAcces.vue";
import ModifierReferentielView from "@/views/Directeur/ModifierReferentielView.vue";
import HistoriqueView from "@/views/Directeur/HistoriqueView.vue";
import PointCommuleView from "@/views/Directeur/PointCommuleView.vue";


import DashboardEtudiantView from "@/views/Etudiant/DashboardEtudiantView.vue";
import SaisirFicheView from "@/views/Etudiant/SaisirFicheView.vue";
import HistoriqueFichesView from "@/views/Etudiant/HistoriqueFichesView.vue";
import ProfilEtudiantView from "@/views/Etudiant/ProfilEtudiantView.vue";
import NotificationsEtudiantView from "@/views/Etudiant/NotificationsView.vue";
import DispositifView from "@/views/Etudiant/DispositifView.vue";
import DemandeView from "@/views/Etudiant/DemandeView.vue";

import ServiceScolarite from "@/views/ServiceScolarite/ServiceScolarite.vue";

import ReferentAcceuil from "@/views/Referent/ReferentAcceuil.vue";
import HistoriqueReferent from "@/views/Referent/HistoriqueReferent.vue";
import ProfilReferentView from "@/views/Referent/ProfilReferentView.vue";

const routes = [
  // Route de la page de connexion (toujours affichée au lancement)
  { path: "/login", name: "Login", component: LoginView },

  // Rediriger la racine vers le login
  { path: "/", redirect: "/login" },

  {
    path: "/directeur",
    name: "Directeur",
    redirect: "/directeur/accueil",
    children: [
      { path: "accueil", name: "AccueilDirecteur", component: AccueilView },
      { path: "notifications", name: "Notifications", component: NotificationsView },
      { path: "sessions", name: "Sessions", component: SessionsView },
      { path: "profil", name: "ProfilView", component: ProfilView },
      { path: "referent", name: "ReferentView", component: ReferentView },
      { path: "attribuer-points", name: "AttribuerPoints", component: AttribuerPointsView },
      { path: "modifier-acces", name: "ModifierAcces", component: ModifierAcces },
      { path: "modifier-referentiel", name: "ModifierReferentiel", component: ModifierReferentielView },
      { path: "historique", name: "Historique", component: HistoriqueView },
      { path: "points-cumules", name: "PointsCumules", component: PointCommuleView },
    ],
  },

  {
    path: "/etudiant",
    name: "Etudiant",
    redirect: "/etudiant/accueil",
    children: [
      { path: "accueil", name: "AccueilEtudiant", component: DashboardEtudiantView },
      { path: "saisir-fiche", name: "SaisirFiche", component: SaisirFicheView },
      { path: "historique-fiches", name: "HistoriqueFiches", component: HistoriqueFichesView },
      { path: "profil", name: "ProfilEtudiant", component: ProfilEtudiantView },
      { path: "notifications", name: "NotificationsEtudiant", component: NotificationsEtudiantView },
      { path: "dispositif", name: "DispositifEtudiant", component: DispositifView },
      { path: "mes-demandes", name: "DemandeEtudiant", component: DemandeView },

    ],
  },

  {
    path: "/scolarite",
    name: "Scolarite",
    redirect: "/scolarite/accueil",
    children: [
      { path: "accueil", name: "AccueilScolarite", component: ServiceScolarite },
    ],
  },

  {
    path: "/referent",
    name: "Referent",
    redirect: "/referent/accueil",
    children: [
      { path: "accueil", name: "AccueilReferent", component: ReferentAcceuil },
      { path: "notifications", name: "NotificationsReferentt", component: NotificationsEtudiantView },
      { path: "profil", name: "ProfilReferent", component: ProfilReferentView },
      { path: "historique", name: "HistoriqueReferent", component: HistoriqueReferent },
    ],
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
