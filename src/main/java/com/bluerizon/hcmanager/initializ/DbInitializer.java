// 
// Decompiled by Procyon v0.5.36
// 

package com.bluerizon.hcmanager.initializ;

import com.bluerizon.hcmanager.models.Profils;
import com.bluerizon.hcmanager.models.TypePatients;
import com.bluerizon.hcmanager.models.Utilisateurs;
import com.bluerizon.hcmanager.repository.ProfilsRepository;
import com.bluerizon.hcmanager.repository.TypePatientsRepository;
import com.bluerizon.hcmanager.repository.UtilisateursRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@ConditionalOnProperty(name = { "app.db-init" }, havingValue = "true")
public class DbInitializer implements CommandLineRunner
{
    private UtilisateursRepository utilisateursRepository;
    private ProfilsRepository profilsRepository;
    private TypePatientsRepository typePatientsRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public DbInitializer(UtilisateursRepository utilisateursRepository,
                         ProfilsRepository profilsRepository,
                         TypePatientsRepository typePatientsRepository) {
        this.utilisateursRepository = utilisateursRepository;
        this.profilsRepository = profilsRepository;
        this.typePatientsRepository = typePatientsRepository;
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

        System.out.println(" -- Database has been initialized");
    }
}
