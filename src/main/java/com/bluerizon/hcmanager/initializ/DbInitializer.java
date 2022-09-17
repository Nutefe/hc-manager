// 
// Decompiled by Procyon v0.5.36
// 

package com.bluerizon.hcmanager.initializ;

import com.bluerizon.hcmanager.models.*;
import com.bluerizon.hcmanager.payload.helper.EnglishNumber;
import com.bluerizon.hcmanager.payload.helper.FrenchNumber;
import com.bluerizon.hcmanager.payload.helper.Helpers;
import com.bluerizon.hcmanager.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@ConditionalOnProperty(name = { "app.db-init" }, havingValue = "true")
public class DbInitializer implements CommandLineRunner
{
    private UtilisateursRepository utilisateursRepository;
    private ProfilsRepository profilsRepository;
    private TypePatientsRepository typePatientsRepository;
    private AssurancesRepository assurancesRepository;
    private FacturesRepository facturesRepository;
    private DocumentsRepository documentsRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public DbInitializer(UtilisateursRepository utilisateursRepository,
                         ProfilsRepository profilsRepository,
                         TypePatientsRepository typePatientsRepository,
                         AssurancesRepository assurancesRepository,
                         FacturesRepository facturesRepository,
                         DocumentsRepository documentsRepository) {
        this.utilisateursRepository = utilisateursRepository;
        this.profilsRepository = profilsRepository;
        this.typePatientsRepository = typePatientsRepository;
        this.assurancesRepository = assurancesRepository;
        this.facturesRepository = facturesRepository;
        this.documentsRepository = documentsRepository;
    }
    
    public void run(final String... args) throws Exception {

        Set<Profils> profils = new HashSet<>();

        Profils profil1 = new Profils();
        profil1.setId(1);
        profil1.setLibelle("PROFIL_SYSTEM");

        if (!this.profilsRepository.existsById(1)){
            Profils sys = this.profilsRepository.save(profil1);
             profils.add(sys);
        }

        Profils profil2 = new Profils();
        profil2.setId(2);
        profil2.setLibelle("PROFIL_DIRECTEUR");

        if (!this.profilsRepository.existsById(2)){
            Profils dir = this.profilsRepository.save(profil2);
            profils.add(dir);
        }

        Profils profil3 = new Profils();
        profil3.setId(3);
        profil3.setLibelle("PROFIL_SECRETAIRE");


        if (!this.profilsRepository.existsById(3)){
            Profils sec = this.profilsRepository.save(profil3);
            profils.add(sec);
        }
        Profils profil4 = new Profils();
        profil4.setId(4);
        profil4.setLibelle("PROFIL_CAISSE");


        if (!this.profilsRepository.existsById(4)){
            Profils cai = this.profilsRepository.save(profil4);
            profils.add(cai);
        }
        Profils profil5 = new Profils();
        profil5.setId(5);
        profil5.setLibelle("PROFIL_GESTIONNAIRE");


        if (!this.profilsRepository.existsById(5)){
            Profils ges = this.profilsRepository.save(profil5);
            profils.add(ges);
        }

        if (profilsRepository.count()>0){
            Utilisateurs utilisateurs = new Utilisateurs();
            utilisateurs.setUsername("sysadmin");
            utilisateurs.setEmail("sysadmin@gmail.com");
            utilisateurs.setNom("sysadmin");
            utilisateurs.setPrenom("sysadmin");
            utilisateurs.setPassword(passwordEncoder.encode("@sys@#123"));
            utilisateurs.setProfils(profils);

            if (!this.utilisateursRepository.findByEmail("sysadmin@gmail.com").isPresent()){
                this.utilisateursRepository.save(utilisateurs);
            }
        }

        if (this.typePatientsRepository.count() <= 0){

            TypePatients typePatient1 = new TypePatients();
            typePatient1.setLibelle("ASSURER INAM");
            TypePatients typePatient2 = new TypePatients();
            typePatient2.setLibelle("ASSURER AUTRE");
            TypePatients typePatient3 = new TypePatients();
            typePatient3.setLibelle("NON ASSURER");
            List<TypePatients> typePatientsList = new ArrayList<>();
            typePatientsList.add(typePatient1);
            typePatientsList.add(typePatient2);
            typePatientsList.add(typePatient3);
            this.typePatientsRepository.saveAll(typePatientsList);
        }
        if (this.assurancesRepository.count() <= 0){
            Assurances assurance = new Assurances();
            assurance.setLibelle("INAM");
            List<Assurances> assurancesList = new ArrayList<>();
            assurancesList.add(assurance);
            this.assurancesRepository.saveAll(assurancesList);
        }

        if (this.documentsRepository.count()<=0){
            List<Documents> documents = new ArrayList<>();
            documents.add(new Documents("FICHE VIDE", "exemplaire_fiche_e2v.pdf"));
            documents.add(new Documents("BULLETIN D'ANALYSE EXEMPLE", "exemplaire_bulletin_analyse.pdf"));
            documents.add(new Documents("FACTURE PROFORMA EXEMPLE 1", "fiche_facture_proforma1_e2v.pdf"));
            documents.add(new Documents("FACTURE PROFORMA EXEMPLE 2", "fiche_facture_proforma2_e2v.pdf"));
            documents.add(new Documents("FACTURE PROFORMA EXEMPLE 3", "fiche_facture_proforma3_e2v.pdf"));
            documents.add(new Documents("FACTURE PROFORMA EXEMPLE 4", "fiche_facture_proforma4_e2v.pdf"));
            documents.add(new Documents("FICHE ORDONNANCE", "fiche_ordonnace_e2v.pdf"));
            documents.add(new Documents("FICHE ORDONNANCE LUNETTE", "fiche_ordonnance_lunette_e2v.pdf"));
            this.documentsRepository.saveAll(documents);
        }

//        System.out.println(FrenchNumber.convert(Math.round(99.4999999999999999)));
//        System.out.println(FrenchNumber.convert(10000000));
//        System.out.println(EnglishNumber.convert(2147483647));
        System.out.println(" -- Database has been initialized");
    }
}
