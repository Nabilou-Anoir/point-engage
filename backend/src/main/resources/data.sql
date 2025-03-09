
--  table referent
INSERT INTO referent (nom, prenom, email)
VALUES ('Dupont', 'Jean', 'jean.dupont@example.com');

INSERT INTO referent (nom, prenom, email)
VALUES ('Durand', 'Marie', 'marie.durand@example.com');

INSERT INTO referent (nom, prenom, email)
VALUES ('Lefevre', 'Pierre', 'pierre.lefevre@example.com');


-- table referentiel
INSERT INTO referentiel (nom, description)
VALUES ('Informatique', 'Formation en informatique et développement');

INSERT INTO referentiel (nom, description)
VALUES ('Electronique', 'Formation en électronique et microcontrôleurs');

INSERT INTO referentiel (nom, description)
VALUES ('Mécanique', 'Formation en génie mécanique');

INSERT INTO referentiel (id_referentiel, nom, description) VALUES (-1, 'Autre', 'Autre');

-- table action

INSERT INTO action (nom, description_Action, id_referent, id_referentiel, date_Debut_Action, date_Fin_Action)
VALUES ('jpo', 'Participation à un hackathon', 1, 1, '2025-02-01', '2025-02-02');

INSERT INTO action (nom, description_Action, id_referent, id_referentiel, date_Debut_Action, date_Fin_Action)
VALUES ('main à la pate', 'Projet collaboratif', 2, 2, '2025-02-02', '2025-02-03');

INSERT INTO action (nom, description_Action, id_referent, id_referentiel, date_Debut_Action, date_Fin_Action)
VALUES ('petit ingenier', 'Atelier de robotique', 3, 3, '2025-02-03', '2025-02-04');

INSERT INTO action (id_Action, nom, description_Action, date_Debut_Action, date_Fin_Action)
VALUES (-1, 'Autre', 'Autre', NULL, NULL);
-- table etudiant
INSERT INTO etudiant (nom, prenom, email, promotion)
VALUES ('Martin', 'Alice', 'alice.martin@example.com', '2025');

INSERT INTO etudiant (nom, prenom, email, promotion)
VALUES ('Bernard', 'Luc', 'luc.bernard@example.com', '2024');

INSERT INTO etudiant (nom, prenom, email, promotion)
VALUES ('Moreau', 'Emma', 'emma.moreau@example.com', '2025');

--table semestrre
INSERT INTO semestre (date_Debut_Semestre,date_Fin_Semestre, nb_Semestre)
VALUES ('2025-02-01', '2025-06-01','1');

INSERT INTO semestre (date_Debut_Semestre,date_Fin_Semestre, nb_Semestre)
VALUES ('2025-06-02','2025-12-02', 2);

INSERT INTO semestre (date_Debut_Semestre,date_Fin_Semestre, nb_Semestre)
VALUES ('2026-02-03','2025-02-03', 3);

INSERT INTO semestre (date_Debut_Semestre,date_Fin_Semestre, nb_Semestre)
VALUES ('2025-02-04','2025-06-04', 4);

INSERT INTO semestre (date_Debut_Semestre,date_Fin_Semestre, nb_Semestre)
VALUES ('2025-02-01','2025-06-01', 5);

INSERT INTO semestre (date_Debut_Semestre,date_Fin_Semestre, nb_Semestre)
VALUES ('2025-02-01','2025-02-01',6);

INSERT INTO semestre (date_Debut_Semestre, date_Fin_Semestre, nb_Semestre)
VALUES ('2025-12-03', '2026-06-03', 7);

INSERT INTO semestre (date_Debut_Semestre, date_Fin_Semestre, nb_Semestre)
VALUES ('2026-06-04', '2026-12-04', 8);

INSERT INTO semestre (date_Debut_Semestre, date_Fin_Semestre, nb_Semestre)
VALUES ('2027-02-01', '2027-06-01', 9);

INSERT INTO semestre (date_Debut_Semestre, date_Fin_Semestre, nb_Semestre)
VALUES ('2027-06-02', '2027-12-02', 10);

-- table participe

INSERT INTO participe (id_etudiant, id_action, id_semestre, total_Points, nb_Participation, date_Debut_Participation, date_Fin_Participation, description_Participation, statut)
VALUES (1, 1, 1, 0.50, 1, '2025-02-01', '2025-02-02', 'Participation réussie', TRUE);

INSERT INTO participe (id_etudiant, id_action, id_semestre, total_Points, nb_Participation, date_Debut_Participation, date_Fin_Participation, description_Participation, statut)
VALUES (2, 2, 2, 0.00, 2, '2025-02-02', '2025-02-03', 'Participation moyenne', FALSE);

INSERT INTO participe (id_etudiant, id_action, id_semestre, total_Points, nb_Participation, date_Debut_Participation, date_Fin_Participation, description_Participation, statut)
VALUES (3, 3, 3, 0.30, 1, '2025-02-03', '2025-02-04', 'Participation satisfaisante', TRUE);