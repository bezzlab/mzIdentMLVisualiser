initializeToptable = function(dataset, tableId) {
            var dt = $('#' + tableId + "-table").DataTable({
                "oLanguage": {
                    "sStripClasses": "",
                    "sSearch": "",
                    "sSearchPlaceholder": "Enter Keywords Here",
                    "sInfo": "_START_ -_END_ of _TOTAL_",
                    "sLengthMenu": '<span>Rows per page:</span><select class="browser-default">' +
                        '<option value="10">10</option>' +
                        '<option value="20">20</option>' +
                        '<option value="30">30</option>' +
                        '<option value="40">40</option>' +
                        '<option value="50">50</option>' +
                        '<option value="-1">All</option>' +
                        '</select></div>'
                },
                data: dataset
                // order: [[ 5, "asc" ]],
                // bAutoWidth: false
            });
            return dt;
        };


        $(document).ready(function() {

            var proteintable;
            var peptidetable;
            var psmtable;
            var hdaId = "${trans.security.encode_id( hda.id )}";
            var filename = "${hda.file_name}";
            var extension = "mzidentml";
            var dataUrl = "${h.url_for( controller='/api/datasets')}/" + hdaId + "?data_type=" + extension + "&filename=" + filename + "&mode=init" + "&datasetId=" + hdaId;

            // Initialisation section
            $('#progress-bar').openModal();
            $('ul.tabs').tabs();
            psmtable = $('#psm-table').DataTable({});

            // give control to controller API
            // "/api/datasets/33b43b4e7093c91f?data_type=mzidentml",
            $.ajax({
                'url': dataUrl,
                'dataType': "json",
                'success': function(data) {
                        proteintable = $('#protein-table').DataTable({
                            dom: 'Bfrtip',
                            buttons: ['csv', 'print'],
                            "oLanguage": {
                                "sStripClasses": "",
                                "sSearch": "",
                                "sSearchPlaceholder": "Enter Keywords Here",
                                "sInfo": "Showing _START_ -_END_ of _TOTAL_ protein records",
                                "sLengthMenu": '<span>Rows per page:</span><select class="browser-default">' +
                                    '<option value="10">10</option>' +
                                    '<option value="20">20</option>' +
                                    '<option value="50">50</option>' +
                                    '<option value="100">100</option>' +
                                    '<option value="-1">All</option>' +
                                    '</select></div>'
                            },
                            "ajax": "/plugins/visualizations/protviewer/static/data/" + hdaId + "_protein.json",
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
                            }]
                        });

                        peptidetable = $('#peptide-table').DataTable({
                            dom: 'Bfrtip',
                            buttons: ['csv', 'print'],
                            "oLanguage": {
                                "sStripClasses": "",
                                "sSearch": "",
                                "sSearchPlaceholder": "Enter Keywords Here",
                                "sInfo": "Showing _START_ -_END_ of _TOTAL_ peptide records",
                                "sLengthMenu": '<span>Rows per page:</span><select class="browser-default">' +
                                    '<option value="10">10</option>' +
                                    '<option value="20">20</option>' +
                                    '<option value="50">50</option>' +
                                    '<option value="100">100</option>' +
                                    '<option value="-1">All</option>' +
                                    '</select></div>'
                            },
                            "ajax": "/plugins/visualizations/protviewer/static/data/" + hdaId + "_peptide.json",
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
                            //     render : function (columndata, type, row) {
                            //         console.log(columndata);
                            //     // return data != '0';
                            // }
                            }]
                        });
                        //  load metadata from json file by ajax
                        $.get("/plugins/visualizations/protviewer/static/data/" + hdaId + "_metadata.json",
                            function(data) {
                                $("#meta-search").append(data.searchType);
                                $("#meta-software").append(data.softwareList);
                                $("#meta-enzymes").append(data.enzymes);
                                $("#meta-fixed").append(data.fixedModifications);
                                $("#meta-variable").append(data.variableModifications);
                            }
                        );
                        $('#progress-bar').closeModal();
                    } // end of success function
            }); // end of ajax

            // ------------------ Table Row Collapsable -----------------------

            function format(content) {
                return '<div id="one-liner">' + content + '</div>';
            }
            function hasIndex(index, indexarray){
                for (var k = 0; k < indexarray.length; k++) {
                    if(indexarray[j]==index){
                        window.alert("hasIndex:"+True);
                        return  True;
                    }
                }
                return  False;
            }

            function displayModifications(modification, sequence) {
                var indexarray = [];
                var newSequence="";
                var count = 0;
                var isfound = false;
                var content = "";

                // get each modification by spliting by semi-colon
                var splitmodification = modification.split(";");

                // remove last  index (mistakenly) generated from last semicolon
                if(splitmodification.length>0){
                    // remove last element
                    splitmodification = splitmodification.slice(0,splitmodification.length-1);
                }

                // travel through each modification
                for (var i = 0; i < splitmodification.length; i++) {
                    // split each modification by colon and get location part
                    var index = splitmodification[i].split(":")[1];
                    // add each possition as index
                    indexarray[count++] = index-1;
                    // add eacn modification as a list
                    content += '<li class="collection-item">' + splitmodification[i] + '<li>' ;
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
                    newSequence +=(isfound)?'<span class="highlight">'+ sequence[j] +'</span>':sequence[j];
                }
                return '<div id="one-liner"><h6>'+newSequence+'</h6><ul class="collection">'+ content +'</ul></div>';
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
                    row.child(format()).show();
                    tr.addClass('shown');
                }
            });

            function psmDataDisplay(parentdata) {
                var peptideref = parentdata[0];
                var filteredData;
                var psmTable;

                $.ajax({
                    'url': "/plugins/visualizations/protviewer/static/data/" + hdaId + "_psm.json",
                    'success': function(data) {
                        // filter out PSM that are correspondant to selected peptide
                        filteredData = $.grep(data.data, function(element) {
                            return element[0] == parentdata[0];
                        });
                        // recreate PSM table
                        psmTable = $('#psm-table').dataTable().api();
                        psmTable.clear();
                        psmTable.rows.add(filteredData);
                        psmTable.draw();

                    } // end of success
                }); // end of ajax
                return peptideref;
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
                    $('#psm-header').innerHTML = "Peptide Spectrum Match List for : " + peptideData[0];
                    psmDataDisplay(peptideData);
                    // display peptide modification in the child row of the selected peptide
                    // pass modifications and sequence respectively
                    row.child(displayModifications(peptideData[5],peptideData[1])).show();
                    // finally expand the child row
                    tr.addClass('shown');
                }

            });

            $('a.toggle-vis').on( 'click', function (e) {
                e.preventDefault();

                // Get the column API object
                var column = psmtable.column( $(this).attr('data-column') );

                // Toggle the visibility
                column.visible( ! column.visible() );
            } );

        }); // end of document ready