/**
 * Stored procedure to reset passwords
 *
 * To run:
 * EXEC resetPasswords 'ReFlex_Meta'
 */
CREATE PROCEDURE resetPasswords
	@metaDatabaseName NVARCHAR(50)
AS
BEGIN

-- Run this script against the relevant company for which you want to change all passwords to "muppet".
 
declare @ccode nvarchar(20)
select @ccode = code from Company
 
declare @muppet nvarchar(150)
set @muppet = '92270b96553ab1e9911f3a9fb095c4cdeac69bdc975cbcc276b71aad0a196cd789455dd337de9ad71425066affb283d3577c5474e83c6395cc0d76606cb5ad45'
 
declare @sql nvarchar(1500)
select @sql = 'delete from ' + @metaDatabaseName + '.dbo.PasswordHistory where companyCode in (select code from Company)'
exec (@sql)
 
select @sql = 'delete from ' + @metaDatabaseName + '.dbo.Principal where companyCode in (select code from Company)'
exec (@sql)
 
select @sql = 'insert into ' + @metaDatabaseName + '.dbo.Principal (guid, enabled, companyCode, password, additionalIdentifier) select guid, 1, ''' + @ccode + ''', ''' + @muppet + ''', (select additionalIdentifier)
from UserIdentity ui where ui.guid not in (select guid from ' + @metaDatabaseName + '.dbo.Principal)'
exec (@sql)

select @sql = 'update ' + @metaDatabaseName + '..PRINCIPAL set password=''' + @muppet + ''' where companyCode= ''' + code + '''' from Company
exec (@sql)

-- Stop All Passwords Having To Be Changed On First Login:
update UserStatus set oneTimePasswordChanged = 1, firstTimeUser = 0

END
