-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `mydb` DEFAULT CHARACTER SET utf8mb3 ;
USE `mydb` ;

-- -----------------------------------------------------
-- Table `mydb`.`books`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`books` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL DEFAULT NULL,
  `author` VARCHAR(45) NULL DEFAULT NULL,
  `genre` VARCHAR(45) NULL DEFAULT NULL,
  `year` INT NULL DEFAULT NULL,
  `image` VARCHAR(150) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE)
ENGINE = InnoDB
AUTO_INCREMENT = 4
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `mydb`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`user` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL DEFAULT NULL,
  `surname` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE)
ENGINE = InnoDB
AUTO_INCREMENT = 4
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `mydb`.`previous books`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`previous_books` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `id_book` INT NULL,
  `id_user` INT NULL,
  `date_of_start` DATE NULL,
  `date_of_end` DATE NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
  INDEX `id_book_idx` (`id_book` ASC) VISIBLE,
  INDEX `id_user_idx` (`id_user` ASC) VISIBLE,
  CONSTRAINT `id_book`
    FOREIGN KEY (`id_book`)
    REFERENCES `mydb`.`books` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `id_user`
    FOREIGN KEY (`id_user`)
    REFERENCES `mydb`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;
insert books(name,author,genre,year,image) values
('Nadddda','Some Chelik','Fantasy',2001,'https://template.canva.com/EADaopxBna4/1/0/251w-ujD6UPGa9hw.jpg'),
('hjvgefeuf','Deadline','Romance',2010,'https://img.freepik.com/free-vector/abstract-green-business-book-cover-page-brochure-template_1017-13933.jpg'),
('HHHHH','Noraml Guy','Normal',2003,'https://www.adobe.com/express/create/cover/media_1b7327f720408f3da750167464f8cbcd74fa319c5.jpeg?width=400&format=jpeg&optimize=medium'),
('NormalBook','Some Chelik','Fantasy',2001,'https://template.canva.com/EADaopxBna4/1/0/251w-ujD6UPGa9hw.jpg'),
('GoodBook','Deadline','Romance',2010,'https://img.freepik.com/free-vector/abstract-green-business-book-cover-page-brochure-template_1017-13933.jpg'),
('SameOldBook','Noraml Guy','Normal',2003,'https://www.adobe.com/express/create/cover/media_1b7327f720408f3da750167464f8cbcd74fa319c5.jpeg?width=400&format=jpeg&optimize=medium');;
insert user(name,surname)
values ('Dcho','Lokin'),('Peter','Griffin'),('Bilbo','Beggins'),('Fredo','Lokin'),('Loid','Griffin'),('Higro','Logan');
insert previous_books(id_book,id_user,date_of_start,date_of_end)
values (4,6,'2015-07-25',null),(6,5,'2015-08-25','2016-07-25'),(6,4,'2015-03-25',null),
(5,4,'2018-07-25',null),(6,6,'2011-07-25','2012-07-25');
SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;