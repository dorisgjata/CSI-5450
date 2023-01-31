import { Tour as TourType } from "../../app/types";
import {
    Box,
    Button,
    Menu,
    MenuItem,
    IconButton,
    Typography,
    Grid,
    MobileStepper,
    Paper,
    Tooltip,
    Fab,
    ListItemIcon,
    ListItemText,
    Dialog,
    DialogTitle,
    DialogContent,
    DialogActions,
    FormControl,
    List,
    ListItem,
    Checkbox,
} from "@mui/material";
import { useAppSelector } from "../../app/hooks";
import { useState } from "react";
import SwipeableViews from 'react-swipeable-views';
import { KeyboardArrowLeft, KeyboardArrowRight, RoomOutlined, MoreVert, PhotoLibraryOutlined } from "@mui/icons-material";
import { Link, useHistory } from "react-router-dom";
import FavoriteIcon from '@mui/icons-material/Favorite';
import EditIcon from '@mui/icons-material/Edit';
import TextField from '@mui/material/TextField';
import { useUpdateTourMutation, useDeleteTourMutation, useFavoriteTourMutation, useDeleteFavoriteTourMutation, useDeleteFromTourMutation, useGetUserFavoritesQuery } from '../../services/api';
import DeleteIcon from '@mui/icons-material/Delete';
import PlayArrowIcon from '@mui/icons-material/PlayArrow';
import FavoriteBorderIcon from '@mui/icons-material/FavoriteBorder';
import images_urls from "../../data/images_urls";

export function Tour(props: { tour: TourType; isPublic: boolean; }) {
    const { tour, isPublic } = props;
    const { isLoggedIn } = useAppSelector(state => state.auth);
    const [activeStep, setActiveStep] = useState(0);
    const maxSteps = tour.artworks.length;
    const handleNext = () => {
        setActiveStep((prevActiveStep) => prevActiveStep + 1);
    };

    const handleBack = () => {
        setActiveStep((prevActiveStep) => prevActiveStep - 1);
    };

    const handleStepChange = (step: any) => {
        setActiveStep(step);
    };

    const history = useHistory();
    const handleRouting = () => {
        isPublic
            ? history.push(`/public-tours/${tour.tourId}`)
            : history.push(`/my-tours/${tour.tourId}`)
    };

    return (
        <Box sx={{ width: 400, mt: 2 }} >
            <Paper
                sx={{
                    p: 2,
                }}
                elevation={1}>
                <Box component="div" sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                    <Box sx={{ display: 'flex', alignItems: 'center' }}>
                        <Typography component="div" variant="h6" >
                            {tour.tourName?.replace(/['"]+/g, '')} {/* for some reason it was coming wrapped in double quotes - this is just a quick fix */}
                        </Typography>
                    </Box>
                    <Box>
                        {isLoggedIn && FavoriteButton(tour)}
                        {!isPublic && DropdownButton(tour)}
                    </Box>
                </Box>
                <Box component="div" sx={{ display: 'flex', flexDirection: 'row', alignItems: 'center', mt: 1 }}>
                    <PhotoLibraryOutlined />
                    <Typography component="div" variant="subtitle1" ml={1}>
                        {tour.artworks.length < 1 ? `${tour.artworks.length} Start adding artworks to this tour.` : tour.artworks.length}
                    </Typography>
                </Box>
                <SwipeableViews
                    index={activeStep}
                    onChangeIndex={handleStepChange}
                    enableMouseEvents>
                    {tour.artworks.map((artwork, index) => {
                        const onView = artwork.location.physicalLocation !== 'Not on View';
                        const unknown = artwork.location.physicalLocation !== 'Unknown';
                        return (
                            <Grid key={`tour_${index}_${artwork.title}`}>
                                <Box sx={{ display: 'flex', flexDirection: 'column' }}>
                                    <Box>
                                        <Box
                                            sx={{
                                                display: 'flex',
                                                alignItems: 'center',
                                                justifyContent: 'center'
                                            }}
                                        >
                                            <Box
                                                sx={{
                                                    height: 400,
                                                    width: 'auto',
                                                }}
                                                component="img"
                                                src={images_urls[artwork.artworkId] && images_urls[artwork.artworkId].images[0]}
                                            />
                                        </Box>
                                        <Box sx={{ display: 'flex', justifyContent: 'center', float: 'right', mt: '-7%', mr: '5%' }}>
                                            <Fab component={Link}
                                                color='primary'
                                                to={
                                                    isPublic
                                                        ? `/public-tours/${tour.tourId}`
                                                        : `/my-tours/${tour.tourId}`
                                                }
                                                onClick={() => handleRouting()}
                                                size='small'
                                            >
                                                <PlayArrowIcon sx={{ color: 'white' }} />
                                            </Fab>
                                        </Box>
                                    </Box>
                                    <Typography component="div" variant="h6">
                                        {artwork.title}
                                    </Typography>
                                    <Typography variant="subtitle1" color="text.primary" component="div">
                                        {artwork.creator.fullName}
                                    </Typography>
                                    {(onView && unknown) &&
                                        <Box component="div" sx={{ display: 'flex', flexDirection: 'row', alignItems: 'center' }}>
                                            <RoomOutlined />
                                            <Box sx={{ display: 'flex', flexDirection: 'column' }}>
                                                <Typography variant="subtitle2" color="text.secondary" component="div">
                                                    {artwork.location.physicalLocation}
                                                </Typography>
                                                <Typography variant="subtitle2" color="text.secondary" component="div">
                                                    {artwork.location.department}
                                                </Typography>
                                            </Box>
                                        </Box>
                                    }
                                </Box>
                            </Grid>
                        )
                    })}
                </SwipeableViews>
                {tour.artworks.length > 1 &&
                    <MobileStepper
                        steps={maxSteps}
                        position="static"
                        activeStep={activeStep}
                        backButton={
                            <Button
                                size="small"
                                onClick={handleBack}
                                disabled={activeStep === 0}>
                                <KeyboardArrowLeft />
                                Back
                            </Button>
                        }
                        nextButton={
                            <Button size="small"
                                onClick={handleNext}
                                disabled={activeStep === maxSteps - 1}>
                                Next
                                <KeyboardArrowRight />
                            </Button>
                        }
                    />}
            </Paper>
        </Box>
    )
};

const DropdownButton = (tour: TourType) => {
    const [anchorEl, setAnchorEl] = useState<null | HTMLButtonElement>(null);
    const open = Boolean(anchorEl);
    const [modalOpen, setModalOpen] = useState(false);
    const [tourName, setTourName] = useState(tour.tourName);
    const [openArtworksModal, setOpenArtworksModal] = useState(false);
    const initialCheckboxes = tour.artworks.map(artwork => ({ artworkId: artwork.artworkId, checked: false }));
    const [deleteArtworks, setDeleteArtworks] = useState(initialCheckboxes);

    const [
        editTourName
    ] = useUpdateTourMutation();

    const [
        removeTour
    ] = useDeleteTourMutation()

    const [
        deleteArtworkFromTour
    ] = useDeleteFromTourMutation();

    const handleDropdown = (event: React.MouseEvent<HTMLButtonElement, MouseEvent>) => {
        setAnchorEl(event.currentTarget);
    };

    const handleSaveEdit = (tourId: number | null, tourName: string | null) => {
        setModalOpen(false);
        setAnchorEl(null);
        editTourName({ tourName, tourId });
    };

    const handleCloseEdit = () => {
        setModalOpen(false);
        setAnchorEl(null);
        setTourName(tour.tourName);
    }
    const handleCheckboxes = (artworkId: string, index: number) => {
        const updateArtworks = deleteArtworks.map((artwork, i) => i === index ? { artworkId, checked: !artwork.checked } : { artworkId, checked: artwork.checked });
        setDeleteArtworks(updateArtworks);
    }

    const handleSaveDelete = () => {
        const ids = deleteArtworks.forEach(art => {
            if (art.checked) {
                return art.artworkId;
            }
        });
        deleteArtworkFromTour({ tourId: tour.tourId, artworkIds: ids });
        setDeleteArtworks(initialCheckboxes);
        setOpenArtworksModal(false);
        setAnchorEl(null);
    }
    const handleCloseDelete = () => {
        setDeleteArtworks(initialCheckboxes);
        setOpenArtworksModal(false);
        setAnchorEl(null);
    }

    return (
        <>
            <IconButton aria-label="more actions" onClick={handleDropdown}>
                <MoreVert />
            </IconButton>
            <Menu
                id="tour-menu"
                anchorEl={anchorEl}
                open={open}
                onClose={() => setAnchorEl(null)}
            >
                <MenuItem onClick={() => setModalOpen(true)}>
                    <ListItemIcon>
                        <EditIcon />
                    </ListItemIcon>
                    <ListItemText primary='Edit Name' />
                </MenuItem>
                <MenuItem
                    onClick={() => removeTour(tour.tourId)}
                >
                    <ListItemIcon>
                        <DeleteIcon />
                    </ListItemIcon>
                    <ListItemText primary='Delete Tour' />
                </MenuItem>
                {tour.artworks.length !== 0 &&
                    <MenuItem
                        onClick={() => setOpenArtworksModal(true)}
                    >
                        <ListItemIcon>
                            <DeleteIcon />
                        </ListItemIcon>
                        <ListItemText primary='Remove artworks' />
                    </MenuItem>}
            </Menu>

            <Dialog onClose={() => handleCloseEdit()} open={modalOpen}>
                <DialogTitle>Edit Tour Name</DialogTitle>
                <DialogContent>
                    <FormControl component="fieldset">
                        <TextField
                            sx={{ width: '300px' }}
                            defaultValue={tourName}
                            onChange={e => setTourName(e.target.value)}
                            variant='standard'
                        />
                    </FormControl>
                </DialogContent>
                <DialogActions>
                    <Button onClick={() => handleCloseEdit()}>Cancel</Button>
                    <Button
                        onClick={() => handleSaveEdit(tour.tourId, tourName)}>Save</Button>
                </DialogActions>
            </Dialog>
            <Dialog onClose={() => handleCloseDelete()} open={openArtworksModal} >
                <DialogTitle>Remove Artworks From Tour</DialogTitle>
                <DialogContent>
                    <List>
                        {tour.artworks.map((artwork, index) => {
                            const checked = deleteArtworks[index].checked;
                            return (<ListItem
                                secondaryAction={
                                    <Checkbox checked={checked} onChange={() => handleCheckboxes(artwork.artworkId, index)} />
                                }
                            >
                                <ListItemText
                                    primary={artwork.title}
                                />
                            </ListItem>
                            )
                        })}
                    </List>
                </DialogContent>
                <DialogActions>
                    <Button onClick={() => handleCloseDelete()}>Cancel</Button>
                    <Button
                        disabled={!deleteArtworks.some((artwork) => artwork.checked)}
                        onClick={() => handleSaveDelete()}>Save</Button>
                </DialogActions>
            </Dialog >
        </>
    );
}

const FavoriteButton = (tour: TourType) => {

    const [
        addFavorite,
    ] = useFavoriteTourMutation()
    const [
        deleteFavorite,
    ] = useDeleteFavoriteTourMutation()

    const { data: favorites } = useGetUserFavoritesQuery({ skipToken: true });
    const isFavorite = favorites?.favoriteTours.find((item: TourType) => item.tourId === tour.tourId);

    return isFavorite ?
        (<Tooltip title='Unfavorite tour' placement='bottom'>
            <IconButton onClick={() => deleteFavorite(tour.tourId)} >
                <FavoriteIcon color="error" />
            </IconButton>
        </Tooltip>) :
        (<Tooltip title='Favorite tour' placement='bottom'>
            <IconButton onClick={() => addFavorite(tour.tourId)} >
                <FavoriteBorderIcon />
            </IconButton>
        </Tooltip>);

}
