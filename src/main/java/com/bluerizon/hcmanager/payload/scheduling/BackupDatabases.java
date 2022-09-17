package com.bluerizon.hcmanager.payload.scheduling;

import com.bluerizon.hcmanager.dao.CaisseDao;
import com.bluerizon.hcmanager.dao.ReserveDao;
import com.bluerizon.hcmanager.exception.BadRequestException;
import com.bluerizon.hcmanager.models.Caisses;
import com.bluerizon.hcmanager.models.Reserves;
import com.bluerizon.hcmanager.payload.helper.Helpers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Configuration
@EnableScheduling
public class BackupDatabases {

	@Scheduled(cron = "0 0 * * * *")
	public void schedule() throws IOException, InterruptedException {

		System.out.println("Backup Started at " + new Date());

//		Date backupDate = new Date();
//		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
//		String backupDateStr = format.format(backupDate);

		DatabaseUtil.backup(
				"bluerizon",
				"admin1",
				"db_hc_manager",
				Helpers.path_file+ UUID.randomUUID().toString()+".sql"
		);
//		String dbNameList = "db_hc_manager";

//		String fileName = "Daily_DB_Backup";
//		String folderPath = "C:\\home";
//		File f1 = new File(folderPath);
//		f1.mkdir();

//		String saveFileName = UUID.randomUUID() + ".sql";
//		String savePath = Helpers.path_file+ File.separator + saveFileName;
//
//		String executeCmd = "mysqldump -u bluerizon -p admin1 --databases " + dbNameList
//				+ " -r " + savePath;
//
//		Process runtimeProcess = null;
//		try {
//			runtimeProcess = Runtime.getRuntime().exec(executeCmd);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		int processComplete = 0;
//		try {
//			processComplete = runtimeProcess.waitFor();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//
//		if (processComplete == 0) {
//			System.out.println("Backup Complete at " + new Date());
//		} else {
//			System.out.println("Backup Failure");
//		}
	}

//	@Autowired
//	private ReserveDao reserveDao;
//	@Autowired
//	private CaisseDao caisseDao;
//
//	@Scheduled(cron = "*/10 * * * * *")
//	public void reserve() {
//
////		System.out.println("Backup Started at " + new Date());
//		System.out.println(this.reserveDao.countReserves());
//		System.out.println("Local date"+ LocalDate.now());
//		Reserves reserveInit = this.reserveDao.findFirst1ByDeletedFalseOrderByIdDesc();
//
//		System.out.println(reserveInit);
//
////		if (LocalDate.now().getDayOfWeek() != DayOfWeek.SUNDAY ){
////			Caisses caisse = this.caisseDao.findById(reserveInit.getCaisse().getId()).orElseThrow(() -> new RuntimeException("Error: object is not found."));
////			Reserves reserve = new Reserves();
////			reserve.setCaisse(caisse);
////			reserve.setLibelle(reserveInit.getLibelle().toUpperCase());
////			reserve.setHeure(reserveInit.getHeure());
////			reserve.setJours(reserveInit.getJours());
////			reserve.setDateReserve(new Date());
////			reserve.setDateSuivant(Helpers.tomorowDate());
////			reserve.setMontantDefini(reserveInit.getMontantDefini());
//////        Double enc = this.encaissementsDao.montantDate(Helpers.getDateFromString(Helpers.currentDate()));
//////        Double dec = this.decaissementDao.montantDateDecaissements(Helpers.getDateFromString(Helpers.currentDate()));
////
////			Long nd = Helpers.dayBetween(reserveInit.getDateSuivant(), new Date());
////
////			if (nd == 0){
////				if (caisse.getSolde() >= reserveInit.getMontantSuivant()){
////					reserve.setMontantReserve(reserveInit.getMontantSuivant());
////					reserve.setMontantSuivant(reserveInit.getMontantDefini());
////					caisse.setDecaissement(caisse.getDecaissement() + reserveInit.getMontantSuivant());
////					caisse.setSolde(caisse.getSolde() - reserveInit.getMontantSuivant());
////					this.caisseDao.save(caisse);
////				} else {
////					reserve.setMontantReserve(caisse.getSolde());
////					reserve.setMontantSuivant((reserveInit.getMontantSuivant() - caisse.getSolde()) + reserveInit.getMontantDefini());
////					caisse.setDecaissement(caisse.getDecaissement() + caisse.getSolde());
////					caisse.setSolde(caisse.getSolde() - caisse.getSolde());
////					this.caisseDao.save(caisse);
////				}
////				this.reserveDao.save(reserve);
//////			return ResponseEntity.ok(this.reserveDao.save(reserve));
////			}
////			else if (nd > 0) {
////				if (caisse.getSolde() >= (reserveInit.getMontantSuivant()*nd)){
////					reserve.setMontantReserve(reserveInit.getMontantSuivant()*nd);
////					reserve.setMontantSuivant(reserveInit.getMontantDefini());
////					caisse.setDecaissement(caisse.getDecaissement() + (reserveInit.getMontantSuivant()*nd));
////					caisse.setSolde(caisse.getSolde() - (reserveInit.getMontantSuivant()*nd));
////					this.caisseDao.save(caisse);
////				} else {
////					reserve.setMontantReserve(caisse.getSolde());
////					reserve.setMontantSuivant(((reserveInit.getMontantSuivant()*nd) - caisse.getSolde()) + reserveInit.getMontantDefini());
////					caisse.setDecaissement(caisse.getDecaissement() + caisse.getSolde());
////					caisse.setSolde(caisse.getSolde() - caisse.getSolde());
////					this.caisseDao.save(caisse);
////				}
////				this.reserveDao.save(reserve);
//////			return ResponseEntity.ok(this.reserveDao.save(reserve));
////			} else {
//////			return ResponseEntity.badRequest().body(new BadRequestException("Montant superieur au solde de la caisse"));
////			}
////		}
//
//	}
}