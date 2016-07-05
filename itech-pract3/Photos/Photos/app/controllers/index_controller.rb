class IndexController < ApplicationController

    def index
        @images = parseJson(buildQuery('' ,''), true)
    end

    def search
        field1 = params['field1']
        field2 = params['field2']
        @prop1 = params['prop1']
        @prop2 = params['prop2']
        @images = parseJson(buildQuery(field1, field2), false)
    end

    def buildQuery(field1, field2)
        require 'net/http'
        require 'rexml/document'

        url = URI('http://localhost:11004/sparql/query')
        query = 'PREFIX dc: <http://purl.org/dc/elements/1.1/>
        PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
        PREFIX foaf: <http://xmlns.com/foaf/0.1/>

        select * where
        {
           ?picture rdf:type foaf:Image .
           OPTIONAL { ?picture dc:title ?title } .
           OPTIONAL { ?picture dc:description ?description } .
           OPTIONAL { ?picture dc:date ?date } .
           OPTIONAL { ?picture dc:type ?type } .
           OPTIONAL { ?picture dc:coverage ?coverage } .
           OPTIONAL { ?picture dc:subject ?subject} .
           OPTIONAL { ?picture dc:creator ?creator } .'

           unless field1.empty?
               query << 'FILTER (regex(str(?' + @prop1.downcase + '), "'+ field1 + '" , "i" ))'
           end
           unless field2.empty?
               query << 'FILTER (regex(str(?' + @prop2.downcase + '), "'+ field2 + '" , "i" ))'
           end
           query << '}'

           query_args = {'format' => 'json','query' => query}
           url.query = URI.encode_www_form(query_args)
           res = Net::HTTP.get_response(url)
           return URI.unescape(res.body)
    end

    def parseJson(data, populate)
        json = JSON.parse data
        json = json['results']['bindings']
        hash = Hash.new
        if populate
          @@props = []
        end
        json.each do |j|
            pic = Hash.new
            j.keys.each do |key|
                pic[key] =  j[key]['value']
                pic['hr'+key] = j[key]['value'].split('/').last.split(':').last
                unless !populate || @@props.include?(key.capitalize) || key == 'picture'
                    @@props << key.capitalize
                end
                img = hash[pic['picture']]
                unless img.nil? || img[key].nil? || img[key] == pic[key]
                    pic[key] << ', ' + img[key]
                    pic['hr'+key] << ', ' + img['hr'+key].split('/').last.split(':').last
                    logger.debug pic[key]
                end
            end
            hash[pic['picture']] = pic
        end
        return hash
    end
    
    helper_method :props
    def props
      @@props
    end
end
