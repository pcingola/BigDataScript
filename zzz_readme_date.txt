===========

Ensembl Release 75 Databases.

THE ENSEMBL FTP SITE
====================

The latest data is always available via a directory prefixed "current_".
For example "current_fasta" will always point to the latest data files in FASTA format.

The FTP directory has the following basic structure, although not all information is available for each species.

|-- bam         BAM files derived by aligning RNASeq data to the genome 
|
|
|-- bed         GERP constrained element data in BED format 
|
|
|-- data_files  Browse alignment data files by species and assembly
|
|
|-- embl        Gene predictions annotated on genomic DNA slices of 1MB in EMBL format
|    |
|    |-- species
|
|
|-- emf         Alignment dumps in EMF format
|    |
|    |-- ensembl_compara    * protein trees and protein multiple alignments underlying othologue/paralogue alignments
|          |
|          |-- pecan        * Pecan whole genome multiple alignments with conservation scores for selected sets
|          |-- epo          * EPO whole genome multiple alignments with conservation scores for selected sets
|    
|    
|   
|-- fasta       Gene Predictions in FASTA format
|    |
|    |-- ancestral alleles   * Predictions of the ancestral alleles (coordinates correspond to each extant species)   
|    |
|    |-- species
|        |
|        |-- cdna      * Transcript (cDNA) predictions
|	 |-- cds       * Coding sequences
|        |-- dna       * Genomic DNA in assembled entities
|        |-- pep       * Translation (peptide) predictions
|        |-- rna       * Non-coding RNA predictions
|  
|
|-- genbank     Gene predictions annotated on genomic DNA slices of 1MB in GenBank format
|    |
|    |-- species
|
|
|-- gtf         Gene annotation in GTF format
|
|
|-- regulation  GFF files representing reatures built by the Ensembl Regulatory build
|     |
|     |--species    
|
|
|-- solr_search         Indices and configuration files for Ensembl Solr Search
|
|
|
|
|-- variation   Variations in GVF format, VCF format and Variant Effect Predictor data
|    |
|    |-- gvf   * Variations in GVF format
|    |
|    |-- vcf   * Variations in VCF format
|    |
|    |-- VEP   * Cache files for use with the VEP script
|         |
|         |-- arrays * VEP against the variants on various genotyping arrays cache files
|
|-- virtual machine   Ensembl virtual machine
|
|
|-- xml         Alignment dumps in XML format
|    |
|    |-- ensembl-compara  * protein and ncRNA trees and multiple alignments underlying othologue/paralogue alignment
|          |
|          |-homologies
|      
|
|-- mysql       MySQL database table text dumps
     |
     |-- core       General genome annotation information
     |
     |          * Genome sequence asembly
     |          * Ensembl gene predictions
     |          * Ab initio gene predictions
     |          * Marker information
     |          * ...
     |
     |-- otherfeatures      Additional genome annotation
     | 
     |          * Gene predictions based on EST information
     |          * ...
     |
     |-- rnaseq     Gene models built from rnaseq data
     |
     |-- variation  Genetic variation information
     |
     |-- vega       Manually curated gene sets  
     |
     |-- cdna       cDNA to genome alignments based on the latest EMBL databases
     |
     |-- ensembl_compara    Cross-species comparative genomics data:
     |
     |          * Orthologue/paralogue predictions
     |          * Protein families
     |          * Whole genome alignments
     |          * Synteny information
     |
     |-- ensembl_ontology     Gene Ontology database
     |
     |-- ensembl_web_user_db  SQL table definition for server-side user config database
     |
     |-- ensembl_website    Ensembl web site database
     |
     |          * Context-sensitive help articles
     |          * News articles
     |          * Mini-ads
     |
     |-- ensembl-mart Cross-species data mining tables 
     |
     |-- functional_genomics_mart   Regulation data
     |
     |-- genomic_features_mart      Clone data sets
     |
     |-- ontology_mart  Ontology
     |
     |-- sequence_mart  Genome sequences
     |
     |-- snp_mart       Genetic variation information
     |
     |-- vega_mart      Manually curated gene sets

Sun Aug  2 19:17:20 EDT 2015
