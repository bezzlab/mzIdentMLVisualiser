#!/usr/bin/Rsript
# -----------------------------------------------------------------------------------------
# Filename      : PRIDE_filesize_analysis.R
# Authors       : Suresh Hewapathirana
# Date          : 2016-09-02
# Description   : This is a investigation of mzIdentML file size distribution.
#                 mzIdentML file sizes were corrected from PRIDE database upto July 2016
# -----------------------------------------------------------------------------------------

# Load data from CSV File
mzid <- read.csv(file="/Users/sureshhewapathirana/Documents/Projects/ResearchProject/mzIdentMLVisualiser/test/mzIdentML_12_08_2016_mzid.csv", header=TRUE, sep=",")

# Do not use scientific number format, use floating numbers
options(scipen = 999)

# Check data are in correct format
View(mzid)
str(mzid)

# data convert to MB
mzid[,"MB"] <- mzid$Size.Bytes./1000000

# basic statistical analysis 
mean(mzid$MB)
median(mzid$MB)
min(mzid$MB)
max(mzid$MB)

# largets  and smallest file size records
mzid[which((mzid$MB == min(mzid$MB))==TRUE),]
mzid[which((mzid$MB == max(mzid$MB))==TRUE),]

# total number of files
nrow(mzid)
# total number of files smaller than 1 GB
nrow(mzid[which(mzid$MB < 500),])

# ---------------- Data are grouped by experiment [BYTES] ---------------- 

data_by_experiment <- (aggregate(mzid[,2:4], list(mzid$SubmissionId), FUN=mean))
#data_by_experiment$Year <- round(data_by_experiment$Year,0)
View(data_by_experiment)
nrow(data_by_experiment)

mean(data_by_experiment$Size.Bytes.)/1000000

# histograms for data grouped by experiment
hist(data_by_experiment$Size.Bytes./1000000, breaks = 100, main = "MzIdentML File Sizes Distribution", xlab = "File size(Bytes)", ylab = "Number of files")
hist(log(data_by_experiment$Size.Bytes./1000000), breaks = 50, main = "Histogram of mzIdentML File sizes", xlab = "Log of File size(Bytes)", ylab = "Number of files")

# basic statistical analysis
mean(data_by_experiment$Size.Bytes.)/1000000
median(data_by_experiment$Size.Bytes.)/1000000
min(data_by_experiment$Size.Bytes.)/1000000
max(data_by_experiment$Size.Bytes.)/1000000

# histograms for data grouped by experiment
hist(data_by_experiment$x, breaks = 100, main = "MzIdentML File Sizes Distribution", xlab = "File size(Bytes)", ylab = "Number of files")
hist(log(data_by_experiment$x), breaks = 100, main = "Histogram of mzIdentML File sizes", xlab = "Log of File size(MB)", ylab = "Number of files")


# mzIdentML file sizes distribution \nfrom 0 MB - 100 MB range
data <- mzid$Size.Bytes.[which((mzid$Size.Bytes./1000000)<100)]
hist(data , xlab = "File size(MB)", ylab = "No of mzIdentML files",main = "mzIdentML file sizes distribution \nfrom 0 MB - 100 MB range")


# ---------------- Data are grouped by experiment [BYTES] ---------------- 
data_by_experiment <- (aggregate(mzid$Size.Bytes., by=list(Category=mzid$Year), FUN=mean))
data_by_experiment$x <-  as.numeric(data_by_experiment)

# histograms for data grouped by experiment
hist(data_by_experiment$x, breaks = 100, main = "MzIdentML File Sizes Distribution", xlab = "File size(Bytes)", ylab = "Number of files")
hist(log(data_by_experiment$x), breaks = 100, main = "Histogram of mzIdentML File sizes", xlab = "Log of File size(Bytes)", ylab = "Number of files")

# get records from a specific range
View(data_by_experiment[which(data_by_experiment$Size.Bytes.>500000000 & data_by_experiment$Size.Bytes.<599000000),])
View(data_by_experiment[which(data_by_experiment$Size.Bytes.>600000000 & data_by_experiment$Size.Bytes.<699000000),])
View(data_by_experiment[which(data_by_experiment$Size.Bytes.>700000000 & data_by_experiment$Size.Bytes.<799000000),])
View(data_by_experiment[which(data_by_experiment$Size.Bytes.>800000000 & data_by_experiment$Size.Bytes.<899000000),])
View(data_by_experiment[which(data_by_experiment$Size.Bytes.>900000000 & data_by_experiment$Size.Bytes.<999000000),])
# or
View(data_by_experiment[which(mzid$Size.MB.>500 & mzid$Size.MB.<999),])
View(data_by_experiment[which(mzid$Size.MB.>220 & mzid$Size.MB.<240),])

# get records from a specific range
View(mzid[which(mzid$Size.MB.>500 & mzid$Size.MB.<599),])
View(mzid[which(mzid$Size.MB.>600 & mzid$Size.MB.<699),])
View(mzid[which(mzid$Size.MB.>700 & mzid$Size.MB.<799),])
View(mzid[which(mzid$Size.MB.>800 & mzid$Size.MB.<899),])
View(mzid[which(mzid$Size.MB.>900 & mzid$Size.MB.<999),])
# or
View(mzid[which(mzid$Size.MB.>500 & mzid$Size.MB.<999),])
View(mzid[which(mzid$Size.MB.>220 & mzid$Size.MB.<240),])

year_average <- aggregate(data_by_experiment[,4,5], list(data_by_experiment$Year), FUN=sum)