-- Table referent
INSERT INTO referent (nom, prenom, email)
VALUES ('Fleuranceau', 'Manon', 'manon.fleuranceau@univ-jfc.fr');

INSERT INTO referent (nom, prenom, email)
VALUES ('Libouroux', 'Sandrine', 'sandrine.libouroux@univ-jfc.fr');

INSERT INTO referent (nom, prenom, email)
VALUES ('Barreau', 'Sylvain', 'sylvain.barreau@univ-jfc.fr');

-- Table referentiel
INSERT INTO referentiel (nom, description)
VALUES
    ('Engagement associatif', 'Actions en lien avec la vie associative et les responsabilités étudiantes'),
    ('Engagement éducatif', 'Actions liées au mentorat, tutorat et accompagnement scolaire'),
    ('Engagement écologique', 'Actions liées à la transition écologique et à la sensibilisation'),
    ('Engagement promotionnel', 'Actions de promotion de l’école et participation aux événements');

INSERT INTO referentiel (id_referentiel, nom, description)
VALUES (-1, 'Autre', 'Autre');

-- Table action
-- Suppression de la colonne id_referent
INSERT INTO action (nom, description_Action, id_referentiel, date_Debut_Action, date_Fin_Action)
VALUES
-- Engagement Associatif
('Membre du bureau d’une association', 'Participation active en tant que membre du bureau d’une association étudiante.', 1, '2025-01-10', '2025-12-31'),
('Contribution à la vie associative', 'Participation à des projets associatifs sur le campus.', 1, '2025-02-15', '2025-12-31'),
('Engagement hors campus', 'Bénévolat pour des associations externes (ex: Croix Rouge, pompiers volontaires).', 1, '2025-03-01', '2025-12-31'),
-- Engagement Éducatif
('La Main à la Pâte', 'Tutorat et soutien scolaire pour les élèves.', 2, '2025-02-02', '2025-12-31'),
('Mentorat avec AFEV', 'Accompagnement d’élèves en difficulté via le programme AFEV.', 2, '2025-03-10', '2025-12-31'),
('Aide aux devoirs', 'Soutien scolaire pour élèves du primaire et du secondaire.', 2, '2025-04-01', '2025-12-31'),
-- Engagement Écologique
('Fresque du Climat', 'Animation d’ateliers sur le climat et l’écologie.', 3, '2025-03-01', '2025-12-31'),
('Fresque du Numérique', 'Sensibilisation aux enjeux environnementaux du numérique.', 3, '2025-03-20', '2025-12-31'),
('Campus en Transition', 'Organisation d’événements pour une transition écologique sur le campus.', 3, '2025-04-15', '2025-12-31'),
-- Engagement Promotionnel
('Ambassadeur ISIS', 'Représentation de l’école lors d’événements.', 4, '2025-02-01', '2025-12-31'),
('Animation JPO', 'Participation aux Journées Portes Ouvertes pour présenter les formations.', 4, '2025-02-05', '2025-02-06'),
('Encadrement de lycéens', 'Accompagnement de lycéens dans la découverte des études supérieures.', 4, '2025-03-12', '2025-12-31');

INSERT INTO action (id_Action, nom, description_Action, date_Debut_Action, date_Fin_Action)
VALUES (-1, 'Autre', 'Autre', NULL, NULL);

-- Table etudiant
INSERT INTO etudiant (nom, prenom, email, promotion)
VALUES ('Ines', 'Gribaa', 'Ines.Griba@etud.univ-jfc.fr', 'FIE 3');

INSERT INTO etudiant (nom, prenom, email, promotion)
VALUES ('Bernard', 'Luc', 'luc.bernard@etud.univ-jfc.fr', 'FIE 3');

INSERT INTO etudiant (nom, prenom, email, promotion)
VALUES ('Moreau', 'Emma', 'emma.moreau@etud.univ-jfc.fr', 'FIE 4');

INSERT INTO etudiant (nom, prenom, email, promotion)
VALUES
    ('Martin', 'Alice', 'alice.martin@etud.univ-jfc.fr', 'FIE 2'),
    ('Dubois', 'Hugo', 'hugo.dubois@etud.univ-jfc.fr', 'FIE 3'),
    ('Petit', 'Léa', 'lea.petit@etud.univ-jfc.fr', 'FIE 1'),
    ('Garcia', 'Louis', 'louis.garcia@etud.univ-jfc.fr', 'FIE 2'),
    ('Morel', 'Chloé', 'chloe.morel@etud.univ-jfc.fr', 'FIE 3');

-- Table semestre
INSERT INTO semestre (date_Debut_Semestre, date_Fin_Semestre, nb_Semestre, annee_Universitaire)
VALUES ('2024-06-02','2024-12-02', 1, '2024-07-31');

INSERT INTO semestre (date_Debut_Semestre, date_Fin_Semestre, nb_Semestre,annee_Universitaire)
VALUES ('2025-06-03','2025-012-03', 2, '2025-01-31');

INSERT INTO semestre (date_Debut_Semestre, date_Fin_Semestre, nb_Semestre,annee_Universitaire)
VALUES ('2026-06-03','2026-12-03', 3, '2026-01-31');

INSERT INTO semestre (date_Debut_Semestre, date_Fin_Semestre, nb_Semestre,annee_Universitaire)
VALUES ('2026-06-03','2027-12-03', 4, '2027-01-31');

INSERT INTO semestre (date_Debut_Semestre, date_Fin_Semestre, nb_Semestre,annee_Universitaire)
VALUES ('2027-06-03','2028-12-03', 5, '2028-01-31');

INSERT INTO semestre (date_Debut_Semestre, date_Fin_Semestre, nb_Semestre,annee_Universitaire)
VALUES ('2028-06-03','2029-12-03', 6, '2029-01-31');

INSERT INTO semestre (date_Debut_Semestre, date_Fin_Semestre, nb_Semestre, annee_Universitaire)
VALUES ('2029-06-03','2030-12-03', 7, '2030-01-31');

-- Semestre 8
INSERT INTO semestre (date_Debut_Semestre, date_Fin_Semestre, nb_Semestre, annee_Universitaire)
VALUES ('2030-06-03','2031-12-03', 8, '2031-01-31');

-- Semestre 9
INSERT INTO semestre (date_Debut_Semestre, date_Fin_Semestre, nb_Semestre, annee_Universitaire)
VALUES ('2031-06-03','2032-12-03', 9, '2032-01-31');

-- Semestre 10
INSERT INTO semestre (date_Debut_Semestre, date_Fin_Semestre, nb_Semestre, annee_Universitaire)
VALUES ('2032-06-03','2033-12-03', 10, '2033-01-31');










-- Table participe
-- Ajout de la colonne id_referent pour associer éventuellement un référent validant la participation
INSERT INTO participe (id_etudiant, id_action, id_semestre, id_referent, total_Points, nb_Participation, date_Debut_Participation, date_Fin_Participation, description_Participation, statut)
VALUES
-- 🌍 Engagement Associatif : participation validée par le référent avec id 1 (statut TRUE)
(1, 2, 1, 1, 0.25, 1, '2025-04-01', '2025-04-02', 'Participation occasionnelle à un projet associatif', TRUE),

-- 🎓 Engagement Éducatif : participation non validée (id_referent à NULL)
(2, 5, 2, NULL, 0, 1, '2025-03-10', '2025-03-12', 'Participation partielle au tutorat AFEV', NULL),

-- 🌱 Engagement Écologique : participation non validée (id_referent à NULL)
(3, 8, 1, NULL, 0.40, 1, '2025-05-20', '2025-05-21', 'Présence ponctuelle à un atelier Fresque du Climat', FALSE);

INSERT INTO participe (
    id_etudiant, id_action, id_semestre, id_referent,
    total_Points, nb_Participation, date_Debut_Participation, date_Fin_Participation,
    description_Participation, statut, resume_Directeur, remarque_Referent, point_Action, id_referentiel
)
VALUES
    (1, 1, 1, 1, 1.00, 3, '2025-01-15', '2025-12-31',
     'Présidente association étudiante', TRUE,
 'Excellente implication dans la gestion de l’association',
     'Participation validée avec engagement continu',
     'Engagement associatif', 1),

    (2, 4, 2, 2, 0.75, 2, '2025-03-05', '2025-06-10',
     'Participation aux ateliers La Main à la Pâte', TRUE,
     'Bonne contribution dans le tutorat',
     'Ateliers bien menés, présence régulière',
     'Engagement éducatif', 2),

    (3, 7, 2, 3, 0.60, 1, '2025-05-15', '2025-05-17',
     'Organisation d’un atelier numérique', TRUE,
     'A bien animé la fresque numérique',
     'Très bonne pédagogie',
     'Engagement écologique', 3),

    (4, 9, 1, 1, 0.90, 2, '2025-04-10', '2025-11-20',
     'Organisation et animation de Campus en Transition', TRUE,
     'Acteur clé dans les événements campus',
     'Très bon impact écologique',
     'Engagement écologique', 3),

    (5, 11, 2, 2, 0.50, 1, '2025-02-05', '2025-02-06',
     'Stand formation lors de la JPO', TRUE,
     'Bon contact avec les visiteurs',
     'Participation dynamique',
     'Engagement promotionnel', 4);

-- Table role
INSERT INTO role (name) VALUES ('ROLE_ETUDIANT');
INSERT INTO role (name) VALUES ('ROLE_DIRECTEUR');
INSERT INTO role (name) VALUES ('ROLE_REFERENT');
INSERT INTO role (name) VALUES ('ROLE_SERVICE_SCOLARITE');

-------------------------------------------------------------------
-- Insertion d’un nouvel étudiant pour FIE 5
-------------------------------------------------------------------
INSERT INTO etudiant (nom, prenom, email, promotion)
VALUES ('Durand', 'Paul', 'paul.durand@etud.univ-jfc.fr', 'FIE 5');

-------------------------------------------------------------------
-- Insertion des participations (table participe)
-------------------------------------------------------------------

-- Pour FIE 1 (promotion FIE 1 : par exemple, l’étudiante "Petit Léa")
-- On suppose que Petit Léa a été insérée et possède l'ID = 6.
-- Participation en semestre 1 :
INSERT INTO participe (
    id_etudiant, id_action, id_semestre, id_referent,
    total_Points, nb_Participation, date_Debut_Participation, date_Fin_Participation,
    description_Participation, statut, resume_Directeur, remarque_Referent, point_Action, id_referentiel
)
VALUES (
           6, 1, 1, 1,
           0.50, 1, '2024-07-15', '2024-08-15',
           'Participation à un événement associatif', TRUE, 'Bonne implication associative', 'Participation validée', 'Engagement associatif', 1
       );

-- Participation en semestre 2 :
INSERT INTO participe (
    id_etudiant, id_action, id_semestre, id_referent,
    total_Points, nb_Participation, date_Debut_Participation, date_Fin_Participation,
    description_Participation, statut, resume_Directeur, remarque_Referent, point_Action, id_referentiel
)
VALUES (
           6, 2, 2, 1,
           0.75, 2, '2025-02-01', '2025-03-01',
           'Contribution à un projet associatif', TRUE, 'Participation active', 'Engagement continu', 'Engagement associatif', 1
       );

-------------------------------------------------------------------
-- Pour FIE 2 (promotion FIE 2 : par exemple, "Martin Alice" et "Garcia Louis")
-- On suppose Martin Alice a l'ID = 4 et Garcia Louis l'ID = 7.
-- Participation en semestre 3 (pour Martin Alice) :
INSERT INTO participe (
    id_etudiant, id_action, id_semestre, id_referent,
    total_Points, nb_Participation, date_Debut_Participation, date_Fin_Participation,
    description_Participation, statut, resume_Directeur, remarque_Referent, point_Action, id_referentiel
)
VALUES (
           4, 3, 3, 2,
           0.60, 1, '2026-05-10', '2026-06-10',
           'Participation au bénévolat hors campus', TRUE, 'Bonne performance', 'Bon engagement', 'Engagement associatif', 1
       );

-- Participation en semestre 4 (pour Garcia Louis) :
INSERT INTO participe (
    id_etudiant, id_action, id_semestre, id_referent,
    total_Points, nb_Participation, date_Debut_Participation, date_Fin_Participation,
    description_Participation, statut, resume_Directeur, remarque_Referent, point_Action, id_referentiel
)
VALUES (
           7, 4, 4, 2,
           0.80, 2, '2027-01-05', '2027-02-05',
           'Participation aux ateliers éducatifs', TRUE, 'Performance satisfaisante', 'Engagement éducatif validé', 'Engagement éducatif', 2
       );

-------------------------------------------------------------------
-- Pour FIE 3 (promotion FIE 3 : par exemple, "Ines Gribaa" et "Dubois Hugo")
-- On suppose Ines Gribaa a l'ID = 1 et Dubois Hugo a l'ID = 5.
-- Participation en semestre 5 (pour Ines Gribaa) :
INSERT INTO participe (
    id_etudiant, id_action, id_semestre, id_referent,
    total_Points, nb_Participation, date_Debut_Participation, date_Fin_Participation,
    description_Participation, statut, resume_Directeur, remarque_Referent, point_Action, id_referentiel
)
VALUES (
           1, 5, 5, 3,
           1.00, 3, '2027-05-15', '2027-06-15',
           'Participation en tant que mentor dans le programme AFEV', TRUE, 'Mentorat très apprécié', 'Engagement éducatif confirmé', 'Engagement éducatif', 2
       );

-- Participation en semestre 6 (pour Dubois Hugo) :
INSERT INTO participe (
    id_etudiant, id_action, id_semestre, id_referent,
    total_Points, nb_Participation, date_Debut_Participation, date_Fin_Participation,
    description_Participation, statut, resume_Directeur, remarque_Referent, point_Action, id_referentiel
)
VALUES (
           5, 6, 6, 3,
           0.90, 2, '2028-03-10', '2028-04-10',
           'Aide aux devoirs pour élèves', TRUE, 'Bon tutorat', 'Engagement éducatif positif', 'Engagement éducatif', 2
       );

-------------------------------------------------------------------
-- Pour FIE 4 (promotion FIE 4 : par exemple, "Moreau Emma")
-- On suppose Moreau Emma a l'ID = 3.
-- Participation en semestre 7 :
INSERT INTO participe (
    id_etudiant, id_action, id_semestre, id_referent,
    total_Points, nb_Participation, date_Debut_Participation, date_Fin_Participation,
    description_Participation, statut, resume_Directeur, remarque_Referent, point_Action, id_referentiel
)
VALUES (
           3, 7, 7, 1,
           0.70, 1, '2029-05-20', '2029-06-20',
           'Animation d’un atelier Fresque du Climat', TRUE, 'Animation dynamique', 'Engagement écologique', 'Engagement écologique', 3
       );

-- Participation en semestre 8 :
INSERT INTO participe (
    id_etudiant, id_action, id_semestre, id_referent,
    total_Points, nb_Participation, date_Debut_Participation, date_Fin_Participation,
    description_Participation, statut, resume_Directeur, remarque_Referent, point_Action, id_referentiel
)
VALUES (
           3, 8, 8, 1,
           0.85, 2, '2030-02-15', '2030-03-15',
           'Participation active dans Fresque du Numérique', TRUE, 'Bonne implication', 'Engagement écologique confirmé', 'Engagement écologique', 3
       );

-------------------------------------------------------------------
-- Pour FIE 5 (promotion FIE 5 : par exemple, "Durand Paul")
-- On suppose que "Durand Paul" a été inséré et possède l'ID = 9.
-- Participation en semestre 9 :
INSERT INTO participe (
    id_etudiant, id_action, id_semestre, id_referent,
    total_Points, nb_Participation, date_Debut_Participation, date_Fin_Participation,
    description_Participation, statut, resume_Directeur, remarque_Referent, point_Action, id_referentiel
)
VALUES (
           9, 9, 9, 2,
           1.20, 3, '2031-07-10', '2031-08-10',
           'Participation à Campus en Transition', TRUE, 'Excellente organisation', 'Engagement écologique fort', 'Engagement écologique', 3
       );

-- Participation en semestre 10 :
INSERT INTO participe (
    id_etudiant, id_action, id_semestre, id_referent,
    total_Points, nb_Participation, date_Debut_Participation, date_Fin_Participation,
    description_Participation, statut, resume_Directeur, remarque_Referent, point_Action, id_referentiel
)
VALUES (
           9, 10, 10, 2,
           1.10, 2, '2032-09-05', '2032-10-05',
           'Participation lors d’un événement promotionnel', TRUE, 'Bonne représentation', 'Engagement promotionnel validé', 'Engagement promotionnel', 4
       );