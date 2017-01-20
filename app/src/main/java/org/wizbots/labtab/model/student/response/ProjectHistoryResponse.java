package org.wizbots.labtab.model.student.response;


public class ProjectHistoryResponse {
    private ImagineerResponse imagineer;
    private ExplorerResponse explorer;
    private WizardResponse wizard;
    private MasterResponse master;
    private LabCertifiedResponse lab_certified;
    private MakerResponse maker;
    private ApprenticeResponse apprentice;

    public ProjectHistoryResponse() {
    }

    public ProjectHistoryResponse(ImagineerResponse imagineer, ExplorerResponse explorer, WizardResponse wizard, MasterResponse master, LabCertifiedResponse lab_certified, MakerResponse maker, ApprenticeResponse apprentice) {
        this.imagineer = imagineer;
        this.explorer = explorer;
        this.wizard = wizard;
        this.master = master;
        this.lab_certified = lab_certified;
        this.maker = maker;
        this.apprentice = apprentice;
    }

    public ImagineerResponse getImagineer() {
        return imagineer;
    }

    public void setImagineer(ImagineerResponse imagineer) {
        this.imagineer = imagineer;
    }

    public ExplorerResponse getExplorer() {
        return explorer;
    }

    public void setExplorer(ExplorerResponse explorer) {
        this.explorer = explorer;
    }

    public WizardResponse getWizard() {
        return wizard;
    }

    public void setWizard(WizardResponse wizard) {
        this.wizard = wizard;
    }

    public MasterResponse getMaster() {
        return master;
    }

    public void setMaster(MasterResponse master) {
        this.master = master;
    }

    public LabCertifiedResponse getLab_certified() {
        return lab_certified;
    }

    public void setLab_certified(LabCertifiedResponse lab_certified) {
        this.lab_certified = lab_certified;
    }

    public MakerResponse getMaker() {
        return maker;
    }

    public void setMaker(MakerResponse maker) {
        this.maker = maker;
    }

    public ApprenticeResponse getApprentice() {
        return apprentice;
    }

    public void setApprentice(ApprenticeResponse apprentice) {
        this.apprentice = apprentice;
    }
}
