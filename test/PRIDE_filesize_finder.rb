require 'net/ftp'

ftp = Net::FTP.new('ftp.pride.ebi.ac.uk')
ftp.passive = true
ftp.login 'anonymous', 'guest'
ftp.chdir('/pride/data/archive/')
ftp.pwd
ftp.nlst('*').each do |year|
  ftp.chdir(File.join( '/pride/data/archive/', year) ) rescue ''
  ftp.nlst('*').each do |num|
    ftp.chdir(File.join('/pride/data/archive/', year, num)) rescue ''
    ftp.nlst('*').each do |accn|
      ftp.chdir(File.join('/pride/data/archive/', year, num, accn)) rescue ''
      ftp.nlst('*mzid*').each do |file|
        next unless file.match(/mzid$/) #|| file.match(/mzid.gz$/)
        path = File.join('/pride/data/archive/', year, num, accn, file)
        filesize = ftp.size(file)
        File.open('mzIdentML_12_08_2016_mzid.txt', 'a') { |out| out.puts("#{path},#{year},#{accn},#{filesize},#{filesize.to_f/(1000000)}") }
      end
    end
  end
end

p "Done!"
