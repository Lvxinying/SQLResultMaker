SELECT PD.PerformanceId,CD.CountryId,PD.LastUpdateDatetime 
FROM dbo.CompanyIdDimension CD 
FULL OUTER JOIN dbo.PerformanceIdDimension PD 
ON CD.CompanyId = PD.CompanyId LIMIT 20000