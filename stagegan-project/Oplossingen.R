# 1)
#######################################################################
data <- read.csv("ZwarteSpar.csv", header = T)
#######################################################################


# a)
##################################################################
# Bereken de groei over de laatste 5 jaar
dataF = subset(data, Fertilizer == "F")
FHeightDiff = dataF$Height5 - dataF$Height0
dataNF = subset(data, Fertilizer == "NF")
NFHeightDiff = dataNF$Height5 - dataNF$Height0

# Aangezien de groei in lengte een continue variabele is maken
# we hier een histogram van
hist(FHeightDiff, xlab = "Groei (in inch)", ylab = "Frequentie")
hist(NFHeightDiff, xlab = "Groei (in inch)", ylab = "Frequentie")
# Deze zijn niet symmetrisch en hebben pieken dus is het beter 
# om hier de mediaan te gebruiken dan het gemiddelde

medF = median(FHeightDiff)
medNF = median(NFHeightDiff)

barplot(c(medF, medNF), 
        names.arg = c("Bemest", "Onbemest"), 
        ylab = "Hoogte groei over 5 jaar (in inch)", xpd = FALSE)
# We zien in de staafdiagram dat bemeste bomen een grotere groei
# vertonen dan onbemeste bomen. Het verschil is groot, meer dan
# 10 inch. Dit betekend dat de bomen sneller groeien als ze bemest
# worden.

# Om zeker te zijn dat dit niet beinvloed wordt door een derde
# variabele doen we een permutatietoets
# Dit is een eenzijdige toets aangezien we enkel geinteresseerd
# zijn in de toename

M <- 99999
meddiffs <- numeric()
v <- c(FHeightDiff, NFHeightDiff)
for(i in 1:M){
  Fperm <- sample(1:(length(FHeightDiff) + length(NFHeightDiff)),
                  length(FHeightDiff))
  meddiffs[i] <- median(v[Fperm]) - median(v[-Fperm])
}
hist(meddiffs, prob = T, xlab = "Medianenverschil teststatistiek", 
     ylab = "Relatieve frequentie", main = "Eenzijdige permutatietoets", 
     xlim = c(-10, 15))

abline(v = medF-medNF, col="blue", lty = 2)
# Bereken de p waarde
p<-(sum(meddiffs >= (medF-medNF))+1)/(M+1)
p
# Deze is 10^-5, de steekproef levert dus een overtuigend bewijs tegen
# de nulhypothese van een gelijke hoogte groei
#######################################################################

# b)
#######################################################################
# Bereken de groei over de laatste 5 jaar
dataC = subset(data, Competition == "C")
CDiagDiff = dataC$Diameter5 - dataC$Diameter0
dataNC = subset(data, Competition == "NC")
NCDiagDiff = dataNC$Diameter5 - dataNC$Diameter0

# Aangezien de groei in lengte een continue variabele is maken
# we hier een histogram van
hist(CDiagDiff, xlab = "Diameter groei (in inch)", ylab = "Frequentie")
hist(NCDiagDiff, xlab = "Diameter groei (in inch)", ylab = "Frequentie")
# We zien dat de piek veel verkleint eens er geen competitie is wat de
# verdeling consistenter maakt. 

medC = median(CDiagDiff)
medNC = median(NCDiagDiff)

barplot(c(medNC, medC), 
        names.arg = c("Geen competitie", "Competitie"), 
        ylab = "Diameter groei over 5 jaar (in inch)", xpd = FALSE)
# We zien dat er zonder competitie een veel groter groei in diameter
# is, meer dan 1 inch. Dit wil zeggen dat bomen zonder competitie
# sneller groeien in diameter.

# Om zeker te zijn dat dit niet beinvloed wordt door een derde
# variabele doen we een permutatietoets
# Dit is een tweezijdige toets aangezien er niet gespecifieerd
# is wat het effect moet zijn
M <- 99999
meddiffs <- double()
v <- c(NCDiagDiff, CDiagDiff)
for(i in 1:M){
  Cperm <- sample(1:(length(CDiagDiff) + length(NCDiagDiff)), length(CDiagDiff))
  meddiffs[i] <- median(v[Cperm]) - median(v[-Cperm])
}
hist(meddiffs, prob = T, xlab = "Medianenverschil teststatistiek",
     ylab = "Relatieve frequentie", main = "Tweezijdige permutatietoets")

abline(v = medNC-medC, col = 'blue', lty = 2)
# Bereken de p waarde, vermenigvuldig met 2 aangezien het
# een tweezijdige permutatietoets is
p<-2*((sum(meddiffs >= (medNC-medC))+1)/(M+1))
p

# Hoeweel de p-waarde deze keer iets hoger ligt is dit nog altijd 
# een lage waarde. Dit zorgt er dus voor dat de steekproef een 
# overtuigend bewijs vormt tegen de nulhypothese van een gelijke 
# toename in diameter
#######################################################################

# c)
#######################################################################
# Bereken de toenames
heightDiff = data$Height5 - data$Height0
diagDiff = data$Diameter5 - data$Diameter0
# Dit zijn continue variabelen dus gebruiken we covariantie om de 
# associatie te modelleren
cov(heightDiff, diagDiff)
# Bereken toch de correlatie om makkelijker te vergelijken
cor(heightDiff, diagDiff)
# Plot dit in een scatterplot met een smoother
scatter.smooth(heightDiff, diagDiff, xlab = "Verschil in hoogte",
     ylab = "Verschil in diameter")
# We zien dat de smoother lijkt op een rechte, er is dus een lineair 
# verband tussen de toename van de lengte en deze van de diameter.
# Aangezien de correlatie 0.90 is, is dit ook een sterk verband
#######################################################################

# d)
#######################################################################
# Stel het model op, Height5 hangt af van Fertilizer en Competition
model <- lm(data$Height5 ~ data$Fertilizer + data$Competition)
# QQplot p179
plot(model)
summary(model)
# We zien dat niet bemesten een negatief effect heeft en dat de 
# afwezigheid van competitie een positief effect heeft op de groei.
# We zien ook dat de p-waarde extreem laag is (< 2.2e-16),
# de schattingen zijn dus zeker goed
# De determinatie coëfficiënt is 0.66 dus ook het model is redelijk
# goed.
#######################################################################

# e)
#######################################################################
# Breng de hoogte bij start in rekening
model <- lm(data$Height5 - data$Height0 ~ data$Fertilizer + data$Competition)
plot(model)
summary(model)
# De determinatie coëfficiënt is hier net iets hoger wat dit model 
# beter maakt.
#######################################################################

# f)
#######################################################################
# Print de inschatting voor de gemiddeldes voor de vier combinaties
print(paste("Fertilized + Competition: ", model$coeff %*% c(1,F,F)))
print(paste("Fertilized + No competition: ", model$coeff %*% c(1,F,T)))
print(paste("Not fertilized + Competition: ", model$coef %*% c(1,T,F)))
print(paste("Not fertilized + No competition: ", model$coeff %*% c(1,T,T)))
#######################################################################




# 2)
#######################################################################
# Clear the environment
closeAllConnections()
rm(list=ls())
# Read the new data
data <- read.csv("Krabben.csv", header = T)
#######################################################################

# a)
#######################################################################
# Splits de data
dataS = subset(data, satell == "TRUE")
dataNS = subset(data, satell == "FALSE")
# Bepaal het percent vrouwelijke krabben dat een satelliet heeft 
# binnen onze steekproef
dataSPercent = length(dataS$satell)/length(data$satell)
# Bepaal de grenzen van het 95% betrouwbaarheidsinterval
noemer = length(data$satell) + 4
tellerp = (length(dataS$satell) + 2)/noemer
bovengrens = tellerp + 1.96 * sqrt((tellerp*(1-tellerp))/noemer)
ondergrens = tellerp - 1.96 * sqrt((tellerp*(1-tellerp))/noemer)
ondergrens; bovengrens
#######################################################################

# b)
#######################################################################
# Visualiseer de data in histogrammen
hist(dataS$width)
hist(dataNS$width)
# De data is niet symmetrisch dus we gebruiken de mediaan

barplot(c(median(dataS$width), median(dataNS$width)), 
        names.arg = c("Satelliet", "Geen satelliet"), 
        ylab = "Breedte van het schild (in cm)", xpd = FALSE)
# De schildgrootte ligt iets hoger bij de vrouwtjes die over een
# satelliet beschikken. Het verschil is echter heel klein, we wensen
# meer informatie. Deze kan geleverd worden door een boxplot

boxplot(dataS$width, dataNS$width, col="yellow", 
        ylab = "Breedte van het schild (in cm)",
        names = c("Satelliet", "Geen satelliet"))
# De schildgrootte heeft dus duidelijk een effect op het al dan niet
# beschikken over een satelliet of niet.

# Nu willen we weten hoezeer de breedte van het schild verschilt
# We voeren de t-test van Welch uit
t.test(dataS$width, dataNS$width)
#######################################################################

# c)
#######################################################################
# Importeer de bibliotheek die nodig is voor discriminant analyses
library(MASS)
# Voer lineaire en kwadratische discriminant analyses uit
lda = lda(satell ~ ., data)
qda = qda(satell ~ ., data)
# Schatten
plda = predict(lda)
pqda = predict(qda)

# Toon de misclassificatietabellen
lt = table(plda$class, data$satell)
qt = table(pqda$class, data$satell)

(lt[2] + lt[3]) / sum(lt)
# Bereken de kwadratische predictiefout
(qt[2] + qt[3]) / sum(qt)

# Doe een lineaire discriminant analyse op de satelliet data, met
# leave-one-out cross-validation
lda = lda(satell ~ ., data, CV=TRUE)
# Doe een kwadratische discriminant analyse op de satelliet data, met
# leave-one-out cross-validation
qda = qda(satell ~ ., data, CV=TRUE)

# Bereken de leave-one-out cross-validation misclassificatietabel voor 
# lineaire discriminant analyse
lt = table(lda$class, data$satell)
# Bereken de leave-one-out cross-validation misclassificatietabel voor 
# kwadratische discriminant analyse
qt = table(qda$class, data$satell)
lt
qt

# Bereken de lineaire predictiefout
(lt[2] + lt[3]) / sum(lt)
# Bereken de kwadratische predictiefout
(qt[2] + qt[3]) / sum(qt)
#######################################################################
