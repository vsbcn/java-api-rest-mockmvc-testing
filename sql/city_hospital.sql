CREATE TABLE `employees` (
  `employee_id` bigint NOT NULL,
  `department` varchar(45) DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `status` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`employee_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

CREATE TABLE `patients` (
  `patient_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `date_of_birth` date DEFAULT NULL,
  `admitted_by` bigint DEFAULT NULL,
  PRIMARY KEY (`patient_id`),
  KEY `admitted_by_idx` (`admitted_by`),
  FOREIGN KEY (`admitted_by`) REFERENCES `employees` (`employee_id`)
) ENGINE=InnoDB AUTO_INCREMENT=229 DEFAULT CHARSET=utf8mb3;