<script>
    import Cookies from 'js-cookie';
    import { toast } from '@zerodevx/svelte-toast';
    const urlAPI = process.env.URLAPI;

    let fileInput;

    async function handleFileChange(event) {
        let file = event.target.files[0];
        if (!file) return;
        
        const formData = new FormData();
        formData.append('file', file);

        let response = await fetch(urlAPI + `/planning/import`, {
            method: 'POST',
            body: formData,
            headers: {
                Authorization: 'Bearer ' + Cookies.get('token')
            }
        });

        fileInput.value = '';
        
        if (response.status === 201) {
            toast.push('Le fichier a bien été importé', {
			theme: {
				'--toastBackground': '#0000',
				'--toastProgressBackground': '#00a66b',
				'--toastBorderRadius': '15px'
			},
            pausable: true
        });
        } else {
            toast.push("Erreur d'importation, veuillez essayer avec un autre fichier", {
			theme: {
				'--toastBackground': '#000000',
				'--toastProgressBackground': '#eb4e3f',
				'--toastBorderRadius': '15px'
			},
            pausable: true
        });
        }
    }
</script>

<div class="container">
    <label for="file" class="label-file">Importer des créneaux horaires</label>
    <input id="file" type="file" bind:this={fileInput} on:change={handleFileChange} />
</div>

<style lang="scss">
	.container {
		display: flex;
		flex-direction: column;
		align-items: center;

		input {
			display: none;
		}

		.label-file {
			cursor: pointer;
			border-radius: 10px;
			padding: 10px;
			background-color: #3366FF;
			color: white;
		}
	}
</style>
