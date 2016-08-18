require 'net/ftp'

ftp = Net::FTP.new('ftp.pride.ebi.ac.uk')
ftp.passive = true
ftp.login 'anonymous', 'guest'
ftp.chdir('/pride/data/archive/2016/08/PXD004569/')
ftp.pwd
path = '/pride/data/archive/pride/data/archive/2016/08/PXD004569/'
ftp.pwd
ftp.nlst('*mzid*').each do |file|
  next unless file.match(/mzid$/) || file.match(/mzid.gz$/)
  path = File.join('/pride/data/archive/2016/08/PXD004569/',  file)
  p ftp.size(path)
  p path
  p file
  p %x[gzip -l file]
  #p %x[gzip -dc 'CPTAC_LabelFree_P6_2_1Aug14_Pippin_13-04-12_msgfplus.mzid.gz' | wc -c]

end

p "Done!"