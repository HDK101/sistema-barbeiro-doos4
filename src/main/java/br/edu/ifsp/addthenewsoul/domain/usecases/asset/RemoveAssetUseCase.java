package br.edu.ifsp.addthenewsoul.domain.usecases.asset;

import br.edu.ifsp.addthenewsoul.domain.entities.asset.Asset;
import br.edu.ifsp.addthenewsoul.domain.usecases.utils.EntityAlreadyExistsException;

public class RemoveAssetUseCase {

    private AssetDAO assetDAO;

    public RemoveAssetUseCase(AssetDAO assetDAO) {
        this.assetDAO = assetDAO;
    }

    public void removesAsset (Asset asset) {
        if (asset == null) return;
        assetDAO.delete(asset.getId());
    }
}
