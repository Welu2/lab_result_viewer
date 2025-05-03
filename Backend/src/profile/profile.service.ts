import { Injectable, NotFoundException } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import { CreateProfileDto } from './dto/create-profile.dto';
import { UpdateProfileDto } from './dto/update-profile.dto';
import { Profile } from './entities/profile.entity';
import { User } from 'src/users/user.entity';

@Injectable()
export class ProfileService {
  constructor(
    @InjectRepository(Profile)
    private readonly profileRepository: Repository<Profile>,
  ) {}

  async create(data: CreateProfileDto & { user: User }) {
    const profile = this.profileRepository.create(data);
    return this.profileRepository.save(profile);
  }

  async findAll() {
    return this.profileRepository.find({ relations: ['user'] });
  }

  async findOne(id: number) {
    const profile = await this.profileRepository.findOne({
      where: { id },
      relations: ['user'],
    });

    if (!profile) {
      throw new NotFoundException(`Profile with id ${id} not found`);
    }

    return profile;
  }

  async findByUserId(userId: number) {
    const profile = await this.profileRepository.findOne({
      where: { user: { id: userId } },
      relations: ['user'],
    });

    if (!profile) {
      throw new NotFoundException(`Profile for user ${userId} not found`);
    }

    return profile;
  }

  async update(id: number, updateProfileDto: UpdateProfileDto) {
    const profile = await this.findOne(+id);
    const updated = this.profileRepository.merge(profile, updateProfileDto);
    return this.profileRepository.save(updated);
  }

  async remove(id: number) {
    const profile = await this.findOne(+id);
    return this.profileRepository.remove(profile);
  }
  async findByPatientId(patientId: string) {
    const profile = await this.profileRepository.findOne({
      where: { patientId },
      relations: ['user'],
    });

    if (!profile) {
      throw new NotFoundException(
        `Profile with patientId ${patientId} not found`,
      );
    }

    return profile;
  }
}

