<%
    import os
    import ConfigParser

    root = h.url_for( "/" )
    app_root = root + "plugins/visualizations/proviewer/static/"

    galaxy_root_dir = os.getcwd()
    galaxy_ini_file = os.path.join(galaxy_root_dir,'config/mzidentml_setttings.ini')
    config          = ConfigParser.ConfigParser()
    config.read(galaxy_ini_file)

    output_dir      = config.get('MzIdentML', 'output_dir')
    error_report_to = config.get('MzIdentML', 'error_report_sent_to')
    rel_output_dir = config.get('MzIdentML', 'rel_output_dir')
%>
    <!DOCTYPE html>
    <html>
    <head>
        <meta charset="UTF-8">
        <title>MzIdentML Viewer</title>
        <!--Import Google Icon Font-->
        <link href="http://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
        ${h.stylesheet_link( app_root + 'css/materialize.min.css' )}
        ${h.stylesheet_link( app_root + 'css/jquery.dataTables.min.css' )}
        ${h.stylesheet_link( app_root + 'css/proviewer.css' )}

        <!--Let browser know website is optimized for mobile-->
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    </head>
    <body>
        <nav>
            <div class="nav-wrapper blue darken-4" width="100%">
                <a href="" class="col s12 brand-logo center">MzIdentML Viewer</a>
            </div>
        </nav>
        <div class="container">
            <div id="errormessage" class="alert alert-danger" style="display:none">
                <p><strong>Sorry!</strong> An error has occurred. Please report to <a href="mailto:${error_report_to}?Subject=Mzidentml Viewer Plugin Error" target="_top">administrator</a></p>
            </div>
            <!-- Progress bar modal -->
            <div id="progress-bar" class="modal modal-trigger">
                <div class="modal-content">
                    <h5>Please wait 1-3 minutes while this file is being prepared for viewing for the first time</h5>
                    <div class="progress">
                        <div class="indeterminate"></div>
                    </div>
                </div>
            </div>
            <!-- Main section -->
            <br/>
            <div id="tabs" class="row">
                <div class="col s12">
                    <ul class="tabs">
                        <li id="tab-1" class="tab col s3"><a href="#metadata-section">Metadata</a></li>
                        <li id="tab-2" class="tab col s3 text-darken-3"><a class="active " href="#protein-section">Proteins</a></li>
                        <li id="tab-3" class="tab col s3"><a href="#peptide-section">Peptides</a></li>
                    </ul>
                </div>
                <!-- Metadata section -->
                <div id="metadata-section" class="col s12">
                    <table id="metadata-table" class="bordered">
                        <tr>
                            <th>Filename</th>
                            <td>${hda.name}</td>
                        </tr>
                        <tr>
                            <th><a class="tooltipped" data-position="bottom" data-delay="50" data-tooltip="The type of search performed e.g. PMF, Tag searches, MS-MS" style="color: #000000;">Search Type</a></th>
                            <td id="meta-search"></td>
                        </tr>
                        <tr>
                            <th><a class="tooltipped" data-position="bottom" data-delay="50" data-tooltip="The software used for performing the analyses" style="color: #000000">List of Software</a></th>
                            <td id="meta-software"></td>
                        </tr>
                        <tr>
                            <th><a class="tooltipped" data-position="bottom" data-delay="50" data-tooltip="Enzyme used to cleave the proteins into peptides" style="color: #000000">Enzymes</a></th>
                            <td id="meta-enzymes"></td>
                        </tr>
                        <tr>
                            <th><a class="tooltipped" data-position="bottom" data-delay="50" data-tooltip="Modifications that are applied universally.Eg: alkylation of cysteine" style="color: #000000">Fixed Modifications</a></th>
                            <td id="meta-fixed"></td>
                        </tr>
                        <tr>
                            <th><a class="tooltipped" data-position="bottom" data-delay="50" data-tooltip="Modifications that do not apply to all instances of a residue. For example, phosphorylation might affect just one serine in a protein containing many serines and threonines." style="color: #000000">Variable Modifications</a></th>
                            <td id="meta-variable"></td>
                        </tr>
                    </table>
                </div>
                <!-- Protein section -->
                <div id="protein-section" class="col s12">
                    <table id="protein-table" class="display" cellspacing="0" width="60%">
                        <thead>
                            <tr>
                                <th></th>
                                <th>Accession</th>
                                <th>Protein</th>
                                <th>Species</th>
                                <th>#Distinct Peptide</th>
                                <th>Score</th>
                                <th>Protein Coverage(%)</th>
                                <th></th>
                            </tr>
                        </thead>
                        <tbody>
                        </tbody>
                    </table>
                </div>
                <!-- Peptide section -->
                <div id="peptide-section" class="col s12">
                    <table id="peptide-table" class="display" cellspacing="0" width="60%">
                        <thead>
                            <tr>
                                <th></th>
                                <th>ID</th>
                                <th>Sequence</th>
                                <th>Start</th>
                                <th>End</th>
                                <th>Accession</th>
                                <th>Modifications</th>
                                <th>#Modifications</th>
                            </tr>
                        </thead>
                    </table>
                </div>
            </div>
        </div>
        ${h.javascript_link( app_root + 'js/jquery-2.2.1.min.js' )}
        ${h.javascript_link( app_root + 'js/materialize.min.js' )}
        ${h.javascript_link( app_root + 'js/jquery.dataTables.min.js' )}
        ${h.javascript_link( app_root + 'js/datatable-materialize.js' )}

        <!-- export result as csv or print -->
        ${h.javascript_link( app_root + 'js/export/dataTables.buttons.min.js')}
        ${h.javascript_link( app_root + 'js/export/buttons.flash.min.js')}
        ${h.javascript_link( app_root + 'js/export/vfs_fonts.js')}
        ${h.javascript_link( app_root + 'js/export/buttons.html5.min.js')}
        ${h.javascript_link( app_root + 'js/export/buttons.print.min.js')}
        <script type="text/javascript" charset="utf-8">

        $(document).ready(function() {

            var proteintable;
            var peptidetable;
            var psmtable;
            var rootLocation = "${galaxy_root_dir}"; // "/Users/myname/Downloads/galaxy";
            var dataLocation = "${rel_output_dir}";  // "/plugins/visualizations/proviewer/static/data/";
            var hdaId     = "${trans.security.encode_id( hda.id )}";
            var inputFile = "${hda.file_name}";
            var extension = "mzidentml";
            var dataUrl   = "${h.url_for( controller='/api/datasets')}/" + hdaId +
                            "?data_type=" + extension +
                            "&inputFile=" + inputFile +
                            "&event=initial_load" +
                            "&datasetId=" + hdaId +
                            "&root=" + rootLocation;

            console.log('INFO : galaxy root directory : '+rootLocation);
            console.log('INFO : Output directory : '+ dataLocation);
            console.log('INFO : Error report to : '+ "${error_report_to}");
            console.log('INFO : dataUrl : '+ dataUrl);

            // Initialisation section
            $('#progress-bar').openModal();
            $('ul.tabs').tabs();

            // give control to controller API
            // "/api/datasets/33b43b4e7093c91f?data_type=mzidentml",
            $.ajax({
                'url': dataUrl,
                'dataType': "json",
                'error':function(){
                    $("#errormessage").fadeIn(1000);
                },
                'success': function(data) {
                        proteintable = $('#protein-table').DataTable({
                            "oLanguage": {
                                "sStripClasses": "",
                                "sSearch": "",
                                "sSearchPlaceholder": "Enter Search Term Here",
                                "sInfo": "Showing _START_ -_END_ of _TOTAL_ protein records",
                                "sLengthMenu": '<span>Rows per page:</span>'+
                                                '<select class="browser-default">' +
                                                    '<option value="5">5</option>' +
                                                    '<option value="10">10</option>' +
                                                    '<option value="20">20</option>' +
                                                    '<option value="50">50</option>' +
                                                    '<option value="100">100</option>' +
                                                    '<option value="-1">All</option>' +
                                                '</select></div>'
                            },
                            dom: 'frtlipB',
                            buttons: [['csv', 'print']],
                            "ajax": dataLocation + hdaId + "_protein.json",
                            bAutoWidth: false,
                            "columns": [{
                                "className": 'details-control',
                                "orderable": false,
                                "data": {
                                    "data": [0]
                                },
                                "defaultContent": ''
                            }, {
                                "data": [1]
                            }, {
                                "data": [2]
                            }, {
                                "data": [3]
                            }, {
                                "data": [4]
                            }, {
                                "data": [5]
                            }, {
                                "data": [6]
                            }, {
                                "visible": false,
                                "data": [7]
                            }],
                            "fnRowCallback": function( nRow, aData, iDisplayIndex, iDisplayIndexFull ) {
                                if ( aData[7] === true ){
                                    $(nRow).find('td:first').addClass('details-control');
                                }else{
                                   $(nRow).find('td:first').removeClass('details-control');
                                }
                                return nRow;
                                }
                        });

                        peptidetable = $('#peptide-table').DataTable({
                            "oLanguage": {
                                "sStripClasses": "",
                                "sSearch": "",
                                "sSearchPlaceholder": "Enter Search Term Here",
                                "sInfo": "Showing _START_ -_END_ of _TOTAL_ peptide records",
                                "sLengthMenu": '<span>Rows per page:</span>' +
                                                '<select class="browser-default">' +
                                                    '<option value="10">10</option>' +
                                                    '<option value="20">20</option>' +
                                                    '<option value="50">50</option>' +
                                                    '<option value="100">100</option>' +
                                                    '<option value="-1">All</option>' +
                                                '</select></div>'
                            },
                            dom: 'frtlipB',
                            buttons: [['csv', 'print']],
                            "ajax": dataLocation + hdaId + "_peptide.json",
                            bAutoWidth: false,
                            "columns": [{
                                "className": 'details-control',
                                "orderable": false,
                                "data": null,
                                "defaultContent": ''
                            }, {
                                "visible": false,
                                "data": [0]
                            }, {
                                "data": [1]
                            }, {
                                "data": [2]
                            }, {
                                "data": [3]
                            }, {
                                "data": [4]
                            }, {
                                "visible": false,
                                "data": [5]
                            },{
                                "data": [6]
                            }]
                        });
                        //  load metadata from json file by ajax
                        $.get(dataLocation + hdaId + "_metadata.json",
                            function(data) {
                                $("#meta-search").append(data.searchType);
                                $("#meta-software").append(data.softwareList);
                                $("#meta-enzymes").append(data.enzymes);
                                $("#meta-fixed").append(data.fixedModifications);
                                $("#meta-variable").append(data.variableModifications);
                            }
                        );
                        $('.buttons-csv').addClass('waves-effect waves-light btn');
                        $('.buttons-print').addClass('waves-effect waves-light btn');
                        $('#progress-bar').closeModal();
                    } // end of success function
            }); // end of ajax

            // ------------------ Table Row Collapsable -----------------------

            function proteinSequenceDisplay(proteinRow) {
                var dbSeqId = proteinRow[0];
                var accession = proteinRow[1];
                var numbers = "";
                var content = '';
                var dataUrl = "${h.url_for( controller='/api/datasets')}/" + hdaId +
                                "?data_type=" + extension +
                                "&inputFile=" + inputFile +
                                "&event=protein_expand" +
                                "&datasetId=" + hdaId +
                                "&dbSequenceId=" + dbSeqId +
                                "&root=" + rootLocation;
                $.ajax({
                    'url': dataUrl,
                    async:false,
                    'dataType': "text",
                    'success': function(data) {
                        var locations = peptideOfProteinDisplay(accession);
                        for (var j = 1; j < data.length-1; j++) {
                            if (j%60 === 0){
                                content += data[j]+'<br/>';
                                numbers += j + '<br/>';
                                content += getTag(locations,j);
                            }else{
                                content += getTag(locations,j);
                                content += (j%10 === 0)? data[j]+ "&nbsp;" : data[j];
                            }
                        }
                    }}
                );
                return '<div id="one-liner"><table style="width:100px"><tr><td><h6 class="sequence">' + content + '</h6></td><td><h6 class="sequence">'+numbers+'</h6></td></tr></table></div><div class="row"><div id="one-liner"></div></div>';
            }

            function getTag(locations, index) {
                var tag = "";
                // travel though each peptide
                for (var i = 0; i < locations.length; i++) {
                    // closing tag
                    if(locations[i][1] == index-1){
                        tag += '</span>';
                    }
                    // opening tag
                    if(locations[i][0] == index){
                        tag += '<span class="highlight" style="border: 1px solid black;">';
                    }
                }
                return tag;
            }

            function peptideOfProteinDisplay(accession){
                 var filteredData;
                 var ranges ;
                 $.ajax({
                    'url': dataLocation + hdaId + "_peptide.json",
                    async:false,
                    'success': function(data) {
                        // filter out PSM that are correspondant to selected peptide
                        filteredData = $.grep(data.data, function(element) {
                            return element[4] == accession;
                        });
                        ranges = new Array(filteredData.length);
                        for (var j = 0; j < filteredData.length; j++) {
                            var location = new Array(filteredData[j][2], filteredData[j][3]);
                            ranges[j] = location;
                        }
                    } // end of success
                }); // end of ajax
                 return ranges;
            }

            function displayModifications(modification, sequence) {

                var indexarray = [];
                var newSequence="";
                var count = 0;
                var isfound = false;
                var content = "";

                // all the modifications seperated by semi-colon in JSON file
                // Here we split modifications by semi-colon to get all the modifications
                var splitmodification = modification.split(";");

                // remove last  index (mistakenly) generated from last semicolon
                if(splitmodification.length>0){
                    // remove last element which is a semi-colon
                    splitmodification = splitmodification.slice(0,splitmodification.length-1);
                }

                // travel through each modification
                for (var i = 0; i < splitmodification.length; i++) {
                    // split each modification by colon and get location part
                    var index = splitmodification[i].split(":")[1];
                    // add each possition as index
                    indexarray[count++] = index-1;
                }

                // travel though each letter of the peptide sequence
                for (var j = 0; j < sequence.length; j++) {
                    isfound = false;
                    // check with current index has a modification
                    for (var k = 0; k < indexarray.length; k++) {
                        // if modification found, swich flag on
                        if(indexarray[k]==j){
                            isfound = true;
                            break;
                        }
                    }
                    // if there is a modification, highlight it, othervise just add the character without highlight
                    newSequence +=(isfound)?'<a class="tooltipped" data-position="bottom" data-delay="50" data-tooltip="'+ splitmodification[k]+'">'+ sequence[j] +'</a>':sequence[j];
                }
                    return '<div><h4>'+newSequence+'</h4><ul class="collection">'+ content +'</ul></div>';
            }

            // Add event listener for opening and closing peptide sequence to show modifications
            $('#protein-table').on('click', 'td.details-control', function() {

                // find the row of the table
                var tr = $(this).closest('tr');
                var row = proteintable.row(tr);

                if (row.child.isShown()) {
                    // This row is already open - close it
                    row.child.hide();
                    tr.removeClass('shown');
                } else {
                    // Open this row
                    var proteinData = row.data();
                    row.child(proteinSequenceDisplay(row.data())).show();
                    tr.addClass('shown');
                }
            });

            function psmDisplay(peptideRow){

                var peptideref = peptideRow[0];
                var filteredData;
                var psmTableHead = '<div class="container"><table class="responsive-table"><thead><tr><th><a class="tooltipped" data-position="top" data-delay="50" data-tooltip="The charge state of the identified peptide" style="color: #000000">Charge</a></th><th><a class="tooltipped" data-position="top" data-delay="50" data-tooltip="The mass-to-charge value measured in the experiment in Daltons / charge" style="color: #000000">Experimental m/z</a></th><th><a class="tooltipped" data-position="top" data-delay="50" data-tooltip="The theoretical mass-to-charge value calculated for the peptide in Daltons / charge" style="color: #000000">Calculated m/z</a></th><th><a class="tooltipped" data-position="top" data-delay="50" data-tooltip="Difference between experimental and calculated m/z values" style="color: #000000">Error m/z</a></th></tr></thead><tbody>';
                var psmTableTail = '</tbody></table></div>';
                var psmTableContent ="";
                var completeTable;

                $.ajax({
                    'url': dataLocation + hdaId + "_psm.json",
                    async:false,
                    'success': function(data) {
                        // filter out PSM that are correspondant to selected peptide
                        filteredData = $.grep(data.data, function(element) {
                            return element[0] == peptideRow[0];
                        });
                        for(var i = 0; i < filteredData.length; i++) {
                            psmTableContent += '<tr><td>'+filteredData[i][1]+'</td><td>'+filteredData[i][2]+'</td><td>'+filteredData[i][3]+'</td><td style="text-align: right;">'+(filteredData[i][3]-filteredData[i][2]).toFixed(4)+'</td></tr>';
                        }
                         completeTable = psmTableHead + psmTableContent + psmTableTail;

                    } // end of success
                }); // end of ajax
                return completeTable;
            }

            // Add event listener for opening and closing psm  data of the selected peptide
            $('#peptide-table').on('click', 'td.details-control', function() {

                // find the row of the table
                var tr = $(this).closest('tr');
                var row = peptidetable.row(tr);

                if (row.child.isShown()) {
                    // This row is already open - close it
                    row.child.hide();
                    tr.removeClass('shown');
                } else {
                    // Open this row
                    var peptideData = row.data();
                    // display PSM table correspondant to selected peptide
                    // psmDataDisplay(peptideData);
                    // display peptide modification in the child row of the selected peptide
                    // pass modifications and sequence respectively
                    var modif = displayModifications(peptideData[5],peptideData[1]);
                    var psmtab = psmDisplay(peptideData);
                    row.child(modif+psmtab).show();
                      // add tooltips
                    $('.tooltipped').tooltip();
                    // finally expand the child row
                    tr.addClass('shown');
                }
            });
        }); // end of document ready

        $(document).ajaxError(function (event, jqxhr, settings) {
                $("#errormessage").fadeIn(1000);
        });
        </script>
    </body>
    </html>
